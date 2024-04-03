package by.aurorasoft.testapp.it;

import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
import by.aurorasoft.testapp.base.AbstractSpringBootTest;
import by.aurorasoft.testapp.crud.dto.Address;
import by.aurorasoft.testapp.crud.dto.Person;
import by.aurorasoft.testapp.crud.entity.*;
import by.aurorasoft.testapp.crud.repository.ReplicatedAddressRepository;
import by.aurorasoft.testapp.crud.repository.ReplicatedPersonRepository;
import by.aurorasoft.testapp.crud.service.AddressService;
import by.aurorasoft.testapp.crud.service.PersonService;
import by.aurorasoft.testapp.model.AddressName;
import by.aurorasoft.testapp.model.PersonName;
import by.aurorasoft.testapp.util.AddressEntityUtil;
import by.aurorasoft.testapp.util.PersonEntityUtil;
import by.aurorasoft.testapp.util.ReplicatedAddressEntityUtil;
import by.aurorasoft.testapp.util.ReplicatedPersonEntityUtil;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class ReplicationIT extends AbstractSpringBootTest {
    private static final long WAIT_REPLICATING_SECONDS = 10;
    private static final String VIOLATION_UNIQUE_CONSTRAINT_SQL_STATE = "23505";

    @Autowired
    private AddressService addressService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ReplicatedAddressRepository replicatedAddressRepository;

    @Autowired
    private ReplicationRetryConsumeProperty retryConsumeProperty;

    @SpyBean
    private ReplicatedPersonRepository replicatedPersonRepository;

    @Test
    public void personAndAddressShouldBeSaved() {
        final String givenAddressCountry = "Belarus";
        final String givenAddressCity = "Minsk";

        final String givenPersonName = "Vlad";
        final String givenPersonSurname = "Zuev";
        final String givenPersonPatronymic = "Sergeevich";
        final LocalDate givenPersonBirthDate = LocalDate.of(2000, 2, 18);

        final Address givenAddress = createAddress(givenAddressCountry, givenAddressCity);
        final Address actualSavedAddress = addressService.save(givenAddress);

        final Person givenPerson = createPerson(
                givenPersonName,
                givenPersonSurname,
                givenPersonPatronymic,
                givenPersonBirthDate,
                actualSavedAddress
        );
        final Person actualSavedPerson = personService.save(givenPerson);

        waitReplicating();

        final Long expectedSavedAddressId = 1L;
        final Address expectedSavedAddress = new Address(expectedSavedAddressId, givenAddressCountry, givenAddressCity);
        assertEquals(expectedSavedAddress, actualSavedAddress);

        final Long expectedSavedPersonId = 1L;
        final Person expectedSavedPerson = new Person(
                expectedSavedPersonId,
                givenPersonName,
                givenPersonSurname,
                givenPersonPatronymic,
                givenPersonBirthDate,
                expectedSavedAddress
        );
        assertEquals(expectedSavedPerson, actualSavedPerson);

        final ReplicatedAddressEntity expectedReplicatedAddress = new ReplicatedAddressEntity(
                expectedSavedAddressId,
                givenAddressCountry,
                givenAddressCity
        );
        verifySave(expectedReplicatedAddress);

        final ReplicatedPersonEntity expectedReplicatedPerson = new ReplicatedPersonEntity(
                expectedSavedPersonId,
                givenPersonName,
                givenPersonSurname,
                givenPersonBirthDate,
                expectedReplicatedAddress
        );
        verifySave(expectedReplicatedPerson);
    }

    @Test
    public void addressShouldNotBeSavedBecauseOfViolationUniqueConstraint() {
        final Address givenAddress = createAddress("Russia", "Moscow");

        saveExpectingUniqueConstraintViolation(givenAddress);
        waitReplicating();
        verifyReplicatedAddressesCount(8);
    }

    @Test
    public void personsAndAddressShouldBeSaved() {
        final String givenAddressCountry = "Belarus";
        final String givenAddressCity = "Minsk";

        final String givenFirstPersonName = "Vlad";
        final String givenFirstPersonSurname = "Zuev";
        final String givenFirstPersonPatronymic = "Sergeevich";
        final LocalDate givenFirstPersonBirthDate = LocalDate.of(2000, 2, 18);

        final String givenSecondPersonName = "Ivan";
        final String givenSecondPersonSurname = "Ivanov";
        final String givenSecondPersonPatronymic = "Ivanovich";
        final LocalDate givenSecondPersonBirthDate = LocalDate.of(2001, 3, 19);

        final Address givenAddress = createAddress(givenAddressCountry, givenAddressCity);
        final Address actualSavedAddress = addressService.save(givenAddress);

        final List<Person> givenPersons = List.of(
                createPerson(
                        givenFirstPersonName,
                        givenFirstPersonSurname,
                        givenFirstPersonPatronymic,
                        givenFirstPersonBirthDate,
                        actualSavedAddress
                ),
                createPerson(
                        givenSecondPersonName,
                        givenSecondPersonSurname,
                        givenSecondPersonPatronymic,
                        givenSecondPersonBirthDate,
                        actualSavedAddress
                )
        );
        final List<Person> actualSavedPersons = personService.saveAll(givenPersons);

        waitReplicating();

        final Long expectedSavedAddressId = 1L;
        final Address expectedSavedAddress = new Address(expectedSavedAddressId, givenAddressCountry, givenAddressCity);
        assertEquals(expectedSavedAddress, actualSavedAddress);

        final Long expectedFirstSavedPersonId = 1L;
        final Long expectedSecondSavedPersonId = 2L;
        final List<Person> expectedSavedPersons = List.of(
                new Person(
                        expectedFirstSavedPersonId,
                        givenFirstPersonName,
                        givenFirstPersonSurname,
                        givenFirstPersonPatronymic,
                        givenFirstPersonBirthDate,
                        expectedSavedAddress
                ),
                new Person(
                        expectedSecondSavedPersonId,
                        givenSecondPersonName,
                        givenSecondPersonSurname,
                        givenSecondPersonPatronymic,
                        givenSecondPersonBirthDate,
                        expectedSavedAddress
                )
        );
        assertEquals(expectedSavedPersons, actualSavedPersons);

        final ReplicatedAddressEntity expectedReplicatedAddress = new ReplicatedAddressEntity(
                expectedSavedAddressId,
                givenAddressCountry,
                givenAddressCity
        );
        verifySave(expectedReplicatedAddress);

        final List<ReplicatedPersonEntity> expectedReplicatedPersons = List.of(
                new ReplicatedPersonEntity(
                        expectedFirstSavedPersonId,
                        givenFirstPersonName,
                        givenFirstPersonSurname,
                        givenFirstPersonBirthDate,
                        expectedReplicatedAddress
                ),
                new ReplicatedPersonEntity(
                        expectedSecondSavedPersonId,
                        givenSecondPersonName,
                        givenSecondPersonSurname,
                        givenSecondPersonBirthDate,
                        expectedReplicatedAddress
                )
        );
        verifySave(expectedReplicatedPersons);
    }

    @Test
    public void addressesShouldNotBeSavedBecauseOfViolationUniqueConstraint() {
        final List<Address> givenAddresses = List.of(
                createAddress("Russia", "Moscow"),
                createAddress("Belarus", "Minsk")
        );

        saveAllExpectingUniqueConstraintViolation(givenAddresses);
        waitReplicating();
        verifyReplicatedAddressesCount(8);
    }

    @Test
    public void personShouldBeUpdated() {
        final Long givenId = 255L;
        final String givenNewName = "Ivan";
        final String givenNewSurname = "Ivanov";
        final String givenNewPatronymic = "Ivanovich";
        final LocalDate givenNewBirthDate = LocalDate.of(2000, 2, 19);

        final Long givenAddressId = 255L;
        final Address givenAddress = new Address(givenAddressId, "Russia", "Moscow");

        final Person givenNewPerson = new Person(
                givenId,
                givenNewName,
                givenNewSurname,
                givenNewPatronymic,
                givenNewBirthDate,
                givenAddress
        );

        final Person actualUpdatedPerson = personService.update(givenNewPerson);

        waitReplicating();

        assertEquals(givenNewPerson, actualUpdatedPerson);

        final var expectedReplicatedAddress = entityManager.getReference(ReplicatedAddressEntity.class, givenAddressId);
        final ReplicatedPersonEntity expectedReplicatedPerson = new ReplicatedPersonEntity(
                givenId,
                givenNewName,
                givenNewSurname,
                givenNewBirthDate,
                expectedReplicatedAddress
        );
        verifySave(expectedReplicatedPerson);
    }

    @Test
    public void addressShouldNotBeUpdatedBecauseOfViolationUniqueConstraint() {
        final Address givenAddress = new Address(256L, "Russia", "Moscow");

        final List<ReplicatedAddressEntity> replicatedAddressesBeforeUpdate = findReplicatedAddressesOrderedById();
        updateExpectingUniqueConstraintViolation(givenAddress);
        waitReplicating();
        final List<ReplicatedAddressEntity> replicatedAddressesAfterUpdate = findReplicatedAddressesOrderedById();
        checkEqualsReplicatedAddresses(replicatedAddressesBeforeUpdate, replicatedAddressesAfterUpdate);
    }

    @Test
    public void personShouldBeUpdatedPartially() {
        final Long givenId = 255L;

        final String givenNewName = "Ivan";
        final String givenNewSurname = "Ivanov";
        final String givenNewPatronymic = "Ivanovich";
        final PersonName givenPartial = new PersonName(givenNewName, givenNewSurname, givenNewPatronymic);

        final Person actualUpdatedPerson = personService.updatePartial(givenId, givenPartial);

        waitReplicating();

        final LocalDate expectedBirthDate = LocalDate.of(2000, 2, 18);

        final Long expectedAddressId = 255L;
        final Address expectedAddress = new Address(expectedAddressId, "Russia", "Moscow");

        final Person expectedUpdatedPerson = new Person(
                givenId,
                givenNewName,
                givenNewSurname,
                givenNewPatronymic,
                expectedBirthDate,
                expectedAddress
        );
        assertEquals(expectedUpdatedPerson, actualUpdatedPerson);

        final var expectedReplicatedAddress = entityManager.getReference(ReplicatedAddressEntity.class, expectedAddressId);
        final ReplicatedPersonEntity expectedReplicatedPerson = new ReplicatedPersonEntity(
                givenId,
                givenNewName,
                givenNewSurname,
                expectedBirthDate,
                expectedReplicatedAddress
        );
        verifySave(expectedReplicatedPerson);
    }

    @Test
    public void addressShouldNotBeUpdatedPartiallyBecauseOfViolationUniqueConstraint() {
        final Long givenId = 256L;
        final AddressName givenAddressName = new AddressName("Russia", "Moscow");

        final List<ReplicatedAddressEntity> replicatedAddressesBeforeUpdate = findReplicatedAddressesOrderedById();
        updateAddressPartialExpectingUniqueConstraintViolation(givenId, givenAddressName);
        waitReplicating();
        final List<ReplicatedAddressEntity> replicatedAddressesAfterUpdate = findReplicatedAddressesOrderedById();
        checkEqualsReplicatedAddresses(replicatedAddressesBeforeUpdate, replicatedAddressesAfterUpdate);
    }

    @Test
    public void personShouldBeDeleted() {
        final Long givenId = 255L;

        personService.delete(givenId);
        waitReplicating();
        assertTrue(isPersonDeleted(givenId));
    }

    @Test
    public void personShouldNotBeDeletedByNotExistId() {
        personService.delete(MAX_VALUE);
        waitReplicating();
        verifyReplicatedPersonsCount(5);
    }

    @Test
    public void operationsShouldBeExecuted() {
        final List<Runnable> givenOperations = List.of(
                () -> saveAll(
                        createAddress("China", "Hong Kong"),
                        createAddress("China", "Anqing"),
                        createAddress("China", "Bozhou")
                ),
                () -> saveAll(
                        createPerson("Avdifaks", "Kuznetsov", "Vasilievich", LocalDate.of(1995, 7, 2), 1L),
                        createPerson("Vitenka", "Kozar", "Vadimovich", LocalDate.of(1996, 6, 1), 2L),
                        createPerson("Yury", "Sitnikov", "Stepanovich", LocalDate.of(1997, 8, 3), 3L)
                ),
                () -> addressService.save(createAddress("China", "Huainan")),
                () -> saveExpectingUniqueConstraintViolation(createAddress("China", "Huainan")),
                () -> saveExpectingUniqueConstraintViolation(createAddress("Russia", "Moscow")),
                () -> addressService.updatePartial(4L, new AddressName("Belarus", "Gomel")),
                () -> personService.updatePartial(2L, new PersonName("Ivan", "Zuev", "Ivanovich")),
                () -> personService.delete(MAX_VALUE),
                () -> addressService.delete(MAX_VALUE),
                () -> updateAddressPartialExpectingUniqueConstraintViolation(256L, new AddressName("Russia", "Moscow")),
                () -> personService.delete(259L),
                () -> addressService.delete(257L),
                () -> personService.update(createPerson(257L, "Alexandr", "Verbitskiy", "Dmitrievich", LocalDate.of(2000, 5, 20), 256L))
        );

        givenOperations.forEach(Runnable::run);

        waitReplicating();

        final List<AddressEntity> actualAddresses = findAddressesOrderedById();
        final List<AddressEntity> expectedAddresses = List.of(
                new AddressEntity(1L, "China", "Hong Kong"),
                new AddressEntity(2L, "China", "Anqing"),
                new AddressEntity(3L, "China", "Bozhou"),
                new AddressEntity(4L, "Belarus", "Gomel"),
                new AddressEntity(255L, "Russia", "Moscow"),
                new AddressEntity(256L, "America", "Chicago"),
                new AddressEntity(258L, "Austria", "Styria"),
                new AddressEntity(259L, "Austria", "Tyrol"),
                new AddressEntity(260L, "Estonia", "Tallinn"),
                new AddressEntity(261L, "Estonia", "Tartu"),
                new AddressEntity(262L, "Estonia", "Narva"),
                new AddressEntity(263L, "Armenia", "Yerevan")
        );
        checkEqualsAddresses(expectedAddresses, actualAddresses);

        final List<PersonEntity> actualPersons = findPersonsOrderedById();
        final List<PersonEntity> expectedPersons = List.of(
                createPersonEntity(1L, "Avdifaks", "Kuznetsov", "Vasilievich", LocalDate.of(1995, 7, 2), 1L),
                createPersonEntity(2L, "Ivan", "Zuev", "Ivanovich", LocalDate.of(1996, 6, 1), 2L),
                createPersonEntity(3L, "Yury", "Sitnikov", "Stepanovich", LocalDate.of(1997, 8, 3), 3L),
                createPersonEntity(255L, "Vlad", "Zuev", "Sergeevich", LocalDate.of(2000, 2, 18), 255L),
                createPersonEntity(256L, "Vasilii", "Dolzhikov", "Borisovich", LocalDate.of(1980, 3, 15), 255L),
                createPersonEntity(257L, "Alexandr", "Verbitskiy", "Dmitrievich", LocalDate.of(2000, 5, 20), 256L),
                createPersonEntity(258L, "Pashenka", "Kornev", "Filippovich", LocalDate.of(1995, 4, 23), 256L)
        );
        checkEqualsPersons(expectedPersons, actualPersons);

        final List<ReplicatedAddressEntity> actualReplicatedAddresses = findReplicatedAddressesOrderedById();
        final List<ReplicatedAddressEntity> expectedReplicatedAddresses = List.of(
                new ReplicatedAddressEntity(1L, "China", "Hong Kong"),
                new ReplicatedAddressEntity(2L, "China", "Anqing"),
                new ReplicatedAddressEntity(3L, "China", "Bozhou"),
                new ReplicatedAddressEntity(4L, "Belarus", "Gomel"),
                new ReplicatedAddressEntity(255L, "Russia", "Moscow"),
                new ReplicatedAddressEntity(256L, "America", "Chicago"),
                new ReplicatedAddressEntity(258L, "Austria", "Styria"),
                new ReplicatedAddressEntity(259L, "Austria", "Tyrol"),
                new ReplicatedAddressEntity(260L, "Estonia", "Tallinn"),
                new ReplicatedAddressEntity(261L, "Estonia", "Tartu"),
                new ReplicatedAddressEntity(262L, "Estonia", "Narva")
        );
        checkEqualsReplicatedAddresses(expectedReplicatedAddresses, actualReplicatedAddresses);

        final List<ReplicatedPersonEntity> actual = findReplicatedPersonsOrderedById();
        final List<ReplicatedPersonEntity> expected = List.of(
                createReplicatedPersonEntity(1L, "Avdifaks", "Kuznetsov", LocalDate.of(1995, 7, 2), 1L),
                createReplicatedPersonEntity(2L, "Ivan", "Zuev", LocalDate.of(1996, 6, 1), 2L),
                createReplicatedPersonEntity(3L, "Yury", "Sitnikov", LocalDate.of(1997, 8, 3), 3L),
                createReplicatedPersonEntity(255L, "Vlad", "Zuev", LocalDate.of(2000, 2, 18), 255L),
                createReplicatedPersonEntity(256L, "Vasilii", "Dolzhikov", LocalDate.of(1980, 3, 15), 255L),
                createReplicatedPersonEntity(257L, "Alexandr", "Verbitskiy", LocalDate.of(2000, 5, 20), 256L),
                createReplicatedPersonEntity(258L, "Pashenka", "Kornev", LocalDate.of(1995, 4, 23), 256L)
        );
        checkEqualsReplicatedPersons(expected, actual);
    }

    @Test
    public void personShouldBeSavedButNotReplicatedBecauseOfThereIsNoReplicatedAddress() {
        final String givenName = "Petr";
        final String givenSurname = "Ivanov";
        final String givenPatronymic = "Petrovich";
        final LocalDate givenBirthDate = LocalDate.of(2000, 3, 19);
        final Long givenAddressId = 263L;
        final Person givenPerson = createPerson(
                givenName,
                givenSurname,
                givenPatronymic,
                givenBirthDate,
                givenAddressId
        );

        final Person actualSavedPerson = personService.save(givenPerson);
        waitReplicating();

        final Long expectedPersonId = 1L;
        final Person expectedSavedPerson = createPerson(
                expectedPersonId,
                givenName,
                givenSurname,
                givenPatronymic,
                givenBirthDate,
                givenAddressId
        );
        assertEquals(expectedSavedPerson, actualSavedPerson);

        assertFalse(isReplicatedPersonExist(expectedPersonId));

        verify(replicatedPersonRepository, times(retryConsumeProperty.getMaxAttempts()))
                .save(any(ReplicatedPersonEntity.class));
    }

    //TODO: add test with error with one consuming attempt

    private static void waitReplicating() {
        try {
            SECONDS.sleep(WAIT_REPLICATING_SECONDS);
        } catch (final InterruptedException cause) {
            currentThread().interrupt();
        }
    }

    private void verifySave(final ReplicatedAddressEntity expected) {
        verifySave(expected, replicatedAddressRepository, ReplicatedAddressEntityUtil::checkEquals);
    }

    private void verifySave(final ReplicatedPersonEntity expected) {
        verifySave(expected, replicatedPersonRepository, ReplicatedPersonEntityUtil::checkEquals);
    }

    private void verifySave(final List<ReplicatedPersonEntity> expected) {
        verifySave(expected, replicatedPersonRepository, ReplicatedPersonEntityUtil::checkEquals);
    }

    private static <ID extends Comparable<ID>, E extends AbstractEntity<ID>> void verifySave(
            final E expected,
            final JpaRepository<E, ID> repository,
            final BiConsumer<E, E> equalChecker
    ) {
        verifySave(singletonList(expected), repository, equalChecker);
    }

    private static <ID extends Comparable<ID>, E extends AbstractEntity<ID>> void verifySave(
            final List<E> expected,
            final JpaRepository<E, ID> repository,
            final BiConsumer<E, E> equalChecker
    ) {
        final List<ID> ids = mapToIds(expected);
        final List<E> actual = findAllByIdOrderById(ids, repository);
        checkEquals(expected, actual, equalChecker);
    }

    private static <ID> List<ID> mapToIds(final List<? extends AbstractEntity<ID>> entities) {
        return entities.stream()
                .map(AbstractEntity::getId)
                .toList();
    }

    private static <ID extends Comparable<ID>, E extends AbstractEntity<ID>> List<E> findAllByIdOrderById(
            final List<ID> ids,
            final JpaRepository<E, ID> repository
    ) {
        final List<E> entities = repository.findAllById(ids);
        entities.sort(comparing(AbstractEntity::getId));
        return entities;
    }

    private static <E extends AbstractEntity<?>> void checkEquals(final List<E> expected,
                                                                  final List<E> actual,
                                                                  final BiConsumer<E, E> equalChecker) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> equalChecker.accept(expected.get(i), actual.get(i)));
    }

    private static void checkEqualsAddresses(final List<AddressEntity> expected, final List<AddressEntity> actual) {
        checkEquals(expected, actual, AddressEntityUtil::checkEquals);
    }

    private static void checkEqualsPersons(final List<PersonEntity> expected, final List<PersonEntity> actual) {
        checkEquals(expected, actual, PersonEntityUtil::checkEquals);
    }

    private static void checkEqualsReplicatedAddresses(final List<ReplicatedAddressEntity> expected,
                                                       final List<ReplicatedAddressEntity> actual) {
        checkEquals(expected, actual, ReplicatedAddressEntityUtil::checkEquals);
    }

    private static void checkEqualsReplicatedPersons(final List<ReplicatedPersonEntity> expected,
                                                     final List<ReplicatedPersonEntity> actual) {
        checkEquals(expected, actual, ReplicatedPersonEntityUtil::checkEquals);
    }

    private boolean isPersonDeleted(final Long id) {
        return !personService.isExist(id) && !replicatedPersonRepository.existsById(id);
    }

    private static void verifyUniqueConstraintViolation(final DataIntegrityViolationException exception) {
        assertEquals(VIOLATION_UNIQUE_CONSTRAINT_SQL_STATE, getSqlState(exception));
    }

    private static String getSqlState(final DataIntegrityViolationException exception) {
        return ((SQLException) getRootCause(exception)).getSQLState();
    }

    private void saveExpectingUniqueConstraintViolation(final Address address) {
        executeExpectingUniqueConstraintViolation(() -> addressService.save(address));
    }

    private void saveAllExpectingUniqueConstraintViolation(final Collection<Address> addresses) {
        executeExpectingUniqueConstraintViolation(() -> addressService.saveAll(addresses));
    }

    private void updateExpectingUniqueConstraintViolation(final Address address) {
        executeExpectingUniqueConstraintViolation(() -> addressService.update(address));
    }

    private void updateAddressPartialExpectingUniqueConstraintViolation(final Long id, final Object partial) {
        executeExpectingUniqueConstraintViolation(() -> addressService.updatePartial(id, partial));
    }

    private static void executeExpectingUniqueConstraintViolation(final Runnable task) {
        boolean exceptionArisen;
        try {
            task.run();
            exceptionArisen = false;
        } catch (final DataIntegrityViolationException exception) {
            exceptionArisen = true;
            verifyUniqueConstraintViolation(exception);
        }
        assertTrue(exceptionArisen);
    }

    @SuppressWarnings("SameParameterValue")
    private void verifyReplicatedAddressesCount(final long expected) {
        assertEquals(expected, countReplicatedAddresses());
    }

    @SuppressWarnings("SameParameterValue")
    private void verifyReplicatedPersonsCount(final long expected) {
        assertEquals(expected, countReplicatedPersons());
    }

    private long countReplicatedAddresses() {
        return queryForLong("SELECT COUNT(e) FROM ReplicatedAddressEntity e");
    }

    private long countReplicatedPersons() {
        return queryForLong("SELECT COUNT(e) FROM ReplicatedPersonEntity e");
    }

    private long queryForLong(final String hqlQuery) {
        return entityManager.createQuery(hqlQuery, Long.class).getSingleResult();
    }

    private List<AddressEntity> findAddressesOrderedById() {
        return findEntities("SELECT e FROM AddressEntity e ORDER BY e.id", AddressEntity.class);
    }

    private List<PersonEntity> findPersonsOrderedById() {
        return findEntities("SELECT e FROM PersonEntity e ORDER BY e.id", PersonEntity.class);
    }

    private List<ReplicatedAddressEntity> findReplicatedAddressesOrderedById() {
        return findEntities("SELECT e FROM ReplicatedAddressEntity e ORDER BY e.id", ReplicatedAddressEntity.class);
    }

    private List<ReplicatedPersonEntity> findReplicatedPersonsOrderedById() {
        return findEntities("SELECT e FROM ReplicatedPersonEntity e ORDER BY e.id", ReplicatedPersonEntity.class);
    }

    private <E extends AbstractEntity<?>> List<E> findEntities(final String hqlQuery, final Class<E> entityType) {
        return entityManager.createQuery(hqlQuery, entityType).getResultList();
    }

    private static Address createAddress(final String country, final String city) {
        return Address.builder()
                .country(country)
                .city(city)
                .build();
    }

    private static Address createAddress(final Long id) {
        return Address.builder()
                .id(id)
                .build();
    }

    private static Person createPerson(final String name,
                                       final String surname,
                                       final String patronymic,
                                       final LocalDate birthDate,
                                       final Long addressId) {
        return createPerson(name, surname, patronymic, birthDate, createAddress(addressId));
    }

    private static Person createPerson(final String name,
                                       final String surname,
                                       final String patronymic,
                                       final LocalDate birthDate,
                                       final Address address) {
        return Person.builder()
                .name(name)
                .surname(surname)
                .patronymic(patronymic)
                .birthDate(birthDate)
                .address(address)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static Person createPerson(final Long id,
                                       final String name,
                                       final String surname,
                                       final String patronymic,
                                       final LocalDate birthDate,
                                       final Long addressId) {
        return Person.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .patronymic(patronymic)
                .birthDate(birthDate)
                .address(createAddress(addressId))
                .build();
    }

    private static ReplicatedPersonEntity createReplicatedPersonEntity(final Long id,
                                                                       final String name,
                                                                       final String surname,
                                                                       final LocalDate birthDate,
                                                                       final Long addressId) {
        return ReplicatedPersonEntity.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .birthDate(birthDate)
                .address(createReplicatedAddressEntity(addressId))
                .build();
    }

    private static ReplicatedAddressEntity createReplicatedAddressEntity(final Long id) {
        return ReplicatedAddressEntity.builder()
                .id(id)
                .build();
    }

    private static PersonEntity createPersonEntity(final Long id,
                                                   final String name,
                                                   final String surname,
                                                   final String patronymic,
                                                   final LocalDate birthDate,
                                                   final Long addressId) {
        return PersonEntity.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .patronymic(patronymic)
                .birthDate(birthDate)
                .address(createAddressEntity(addressId))
                .build();
    }

    private static AddressEntity createAddressEntity(final Long id) {
        return AddressEntity.builder()
                .id(id)
                .build();
    }

    private void saveAll(final Address... addresses) {
        saveAll(addressService, addresses);
    }

    private void saveAll(final Person... persons) {
        saveAll(personService, persons);
    }

    @SafeVarargs
    private static <T extends AbstractDto<?>> void saveAll(final AbsServiceCRUD<?, ?, T, ?> service, final T... dtos) {
        service.saveAll(asList(dtos));
    }

    private boolean isReplicatedPersonExist(final Long id) {
        return replicatedPersonRepository.existsById(id);
    }
}
