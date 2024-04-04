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
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

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
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class ReplicationIT extends AbstractSpringBootTest {
    private static final long WAIT_REPLICATION_SECONDS = 10;
    private static final String VIOLATION_UNIQUE_CONSTRAINT_SQL_STATE = "23505";

    @Autowired
    private AddressService addressService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ReplicationRetryConsumeProperty retryConsumeProperty;

    @SpyBean
    private ReplicatedAddressRepository replicatedAddressRepository;

    @SpyBean
    private ReplicatedPersonRepository replicatedPersonRepository;

    @Test
    public void addressShouldBeSavedAndReplicated() {
        final String givenCountry = "Belarus";
        final String givenCity = "Minsk";
        final Address givenAddress = createAddress(givenCountry, givenCity);

        final Address actualSaved = executeReplicatedOperation(() -> addressService.save(givenAddress));

        final Long expectedId = 1L;
        final Address expectedSaved = new Address(expectedId, givenCountry, givenCity);
        assertEquals(expectedSaved, actualSaved);

        final ReplicatedAddressEntity expectedReplicated = new ReplicatedAddressEntity(
                expectedId,
                givenCountry,
                givenCity
        );
        verifyReplicatedAddress(expectedReplicated);
    }

    @Test
    public void addressShouldNotBeSavedBecauseOfViolationUniqueConstraint() {
        final Address givenAddress = createAddress("Russia", "Moscow");

        executeReplicatedOperation(() -> saveExpectingUniqueConstraintViolation(givenAddress));

        verifyNoInteractions(replicatedAddressRepository);
    }

    @Test
    public void addressesShouldBeSavedAndReplicated() {
        final String firstGivenCountry = "China";
        final String firstGivenCity = "Fuyang";
        final Address firstGivenAddress = createAddress(firstGivenCountry, firstGivenCity);

        final String secondGivenCountry = "China";
        final String secondGivenCity = "Hefei";
        final Address secondGivenAddress = createAddress(secondGivenCountry, secondGivenCity);

        final List<Address> actualSaved = executeReplicatedOperation(() -> saveAll(firstGivenAddress, secondGivenAddress));

        final Long firstExpectedId = 1L;
        final Long secondExpectedId = 2L;
        final List<Address> expectedSaved = List.of(
                new Address(firstExpectedId, firstGivenCountry, firstGivenCity),
                new Address(secondExpectedId, secondGivenCountry, secondGivenCity)
        );
        assertEquals(expectedSaved, actualSaved);

        final List<ReplicatedAddressEntity> expectedReplicated = List.of(
                new ReplicatedAddressEntity(firstExpectedId, firstGivenCountry, firstGivenCity),
                new ReplicatedAddressEntity(secondExpectedId, secondGivenCountry, secondGivenCity)
        );
        verifyReplicatedAddresses(expectedReplicated);
    }

    @Test
    public void addressesShouldNotBeSavedBecauseOfViolationUniqueConstraint() {
        final List<Address> givenAddresses = List.of(
                createAddress("Russia", "Moscow"),
                createAddress("Belarus", "Minsk")
        );

        executeReplicatedOperation(() -> saveAllExpectingUniqueConstraintViolation(givenAddresses));

        verifyNoInteractions(replicatedAddressRepository);
    }

    @Test
    public void addressShouldBeUpdated() {
        final Long givenId = 255L;
        final String givenNewCountry = "Belarus";
        final String givenNewCity = "Minsk";
        final Address givenAddress = new Address(givenId, givenNewCountry, givenNewCity);

        final Address actualUpdated = executeReplicatedOperation(() -> addressService.update(givenAddress));
        assertEquals(givenAddress, actualUpdated);

        final ReplicatedAddressEntity expectedReplicated = new ReplicatedAddressEntity(
                givenId,
                givenNewCountry,
                givenNewCity
        );
        verifyReplicatedAddress(expectedReplicated);
    }

    @Test
    public void addressShouldNotBeUpdatedBecauseOfViolationUniqueConstraint() {
        final Address givenAddress = new Address(256L, "Russia", "Moscow");

        executeReplicatedOperation(() -> updateExpectingUniqueConstraintViolation(givenAddress));

        verifyNoInteractions(replicatedAddressRepository);
    }

    @Test
    public void addressShouldBeUpdatedPartially() {
        final Long givenId = 255L;
        final String givenNewCountry = "Belarus";
        final String givenNewCity = "Minsk";
        final AddressName givenNewName = new AddressName(givenNewCountry, givenNewCity);

        final Address actualUpdated = executeReplicatedOperation(() -> addressService.updatePartial(givenId, givenNewName));
        final Address expectedUpdated = new Address(givenId, givenNewCountry, givenNewCity);
        assertEquals(expectedUpdated, actualUpdated);

        final ReplicatedAddressEntity expectedReplicated = new ReplicatedAddressEntity(
                givenId,
                givenNewCountry,
                givenNewCity
        );
        verifyReplicatedAddress(expectedReplicated);
    }

    @Test
    public void addressShouldNotBeUpdatedPartiallyBecauseOfViolationUniqueConstraint() {
        final Long givenId = 256L;
        final AddressName givenAddressName = new AddressName("Russia", "Moscow");

        executeReplicatedOperation(() -> updateAddressPartialExpectingUniqueConstraintViolation(givenId, givenAddressName));

        verifyNoInteractions(replicatedAddressRepository);
    }

    @Test
    public void addressShouldBeDeleted() {
        final Long givenId = 262L;

        executeReplicatedOperation(() -> addressService.delete(givenId));

        assertTrue(isAddressDeletedWithReplication(givenId));
    }

    @Test
    public void addressShouldNotBeDeletedByNotExistId() {
        final Long givenId = MAX_VALUE;

        executeReplicatedOperation(() -> addressService.delete(givenId));

        verify(replicatedAddressRepository, times(1)).deleteById(eq(givenId));
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

        executeReplicatedOperation(() -> givenOperations.forEach(Runnable::run));

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
                new ReplicatedAddressEntity(262L, "Estonia", "Narva"),
                new ReplicatedAddressEntity(263L, "America", "Houston")
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
        final Address givenAddress = createAddress(264L);
        final Person givenPerson = createPerson(
                givenName,
                givenSurname,
                givenPatronymic,
                givenBirthDate,
                givenAddress
        );

        final Person actualSaved = executeReplicatedOperation(() -> personService.save(givenPerson));

        final Long expectedId = 1L;
        final Person expectedSaved = new Person(
                expectedId,
                givenName,
                givenSurname,
                givenPatronymic,
                givenBirthDate,
                givenAddress
        );
        assertEquals(expectedSaved, actualSaved);

        assertFalse(replicatedPersonRepository.existsById(expectedId));

        verify(replicatedPersonRepository, times(retryConsumeProperty.getMaxAttempts()))
                .save(any(ReplicatedPersonEntity.class));
    }

    @Test
    public void addressShouldBeSavedButNotReplicatedBecauseOfUniqueConstraint() {
        final String givenCountry = "Japan";
        final String givenCity = "Tokyo";
        final Address givenAddress = createAddress(givenCountry, givenCity);

        final Address actualSaved = executeReplicatedOperation(() -> addressService.save(givenAddress));

        final Long expectedId = 1L;
        final Address expectedSaved = new Address(expectedId, givenCountry, givenCity);
        assertEquals(expectedSaved, actualSaved);

        assertFalse(replicatedAddressRepository.existsById(expectedId));

        verify(replicatedAddressRepository, times(1)).save(any(ReplicatedAddressEntity.class));
    }

    private static <R> R executeReplicatedOperation(final Supplier<R> operation) {
        final R result = operation.get();
        waitReplication();
        return result;
    }

    private static void executeReplicatedOperation(final Runnable task) {
        task.run();
        waitReplication();
    }

    private static void waitReplication() {
        try {
            SECONDS.sleep(WAIT_REPLICATION_SECONDS);
        } catch (final InterruptedException cause) {
            currentThread().interrupt();
        }
    }

    private void verifyReplicatedAddress(final ReplicatedAddressEntity expected) {
        verifyEntity(expected, replicatedAddressRepository, ReplicatedAddressEntityUtil::checkEquals);
    }

    private void verifyReplicatedAddresses(final List<ReplicatedAddressEntity> expected) {
        verifyEntities(expected, replicatedAddressRepository, ReplicatedAddressEntityUtil::checkEquals);
    }

    private static <ID extends Comparable<ID>, E extends AbstractEntity<ID>> void verifyEntity(
            final E expected,
            final JpaRepository<E, ID> repository,
            final BiConsumer<E, E> equalChecker
    ) {
        verifyEntities(singletonList(expected), repository, equalChecker);
    }

    private static <ID extends Comparable<ID>, E extends AbstractEntity<ID>> void verifyEntities(
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

    private boolean isAddressDeletedWithReplication(final Long id) {
        return !addressService.isExist(id) && !replicatedAddressRepository.existsById(id);
    }

    private static void verifyUniqueConstraintViolation(final DataIntegrityViolationException exception) {
        assertEquals(VIOLATION_UNIQUE_CONSTRAINT_SQL_STATE, getSqlState(exception));
    }

    private static String getSqlState(final DataIntegrityViolationException exception) {
        return ((SQLException) getRootCause(exception)).getSQLState();
    }

    private Address saveExpectingUniqueConstraintViolation(final Address address) {
        return executeExpectingUniqueConstraintViolation(() -> addressService.save(address));
    }

    private List<Address> saveAllExpectingUniqueConstraintViolation(final Collection<Address> addresses) {
        return executeExpectingUniqueConstraintViolation(() -> addressService.saveAll(addresses));
    }

    private Address updateExpectingUniqueConstraintViolation(final Address address) {
        return executeExpectingUniqueConstraintViolation(() -> addressService.update(address));
    }

    private Address updateAddressPartialExpectingUniqueConstraintViolation(final Long id, final Object partial) {
        return executeExpectingUniqueConstraintViolation(() -> addressService.updatePartial(id, partial));
    }

    private static <R> R executeExpectingUniqueConstraintViolation(final Callable<R> task) {
        R result = null;
        boolean dataIntegrityViolationExceptionArisen;
        try {
            result = task.call();
            dataIntegrityViolationExceptionArisen = false;
        } catch (final DataIntegrityViolationException exception) {
            dataIntegrityViolationExceptionArisen = true;
            verifyUniqueConstraintViolation(exception);
        } catch (final Exception cause) {
            throw new RuntimeException(cause);
        }
        assertTrue(dataIntegrityViolationExceptionArisen);
        return result;
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

    private List<Address> saveAll(final Address... addresses) {
        return saveAll(addressService, addresses);
    }

    private void saveAll(final Person... persons) {
        saveAll(personService, persons);
    }

    @SafeVarargs
    private static <T extends AbstractDto<?>> List<T> saveAll(final AbsServiceCRUD<?, ?, T, ?> service, final T... dtos) {
        return service.saveAll(asList(dtos));
    }

    private boolean isReplicatedPersonExist(final Long id) {
        return replicatedPersonRepository.existsById(id);
    }

    private boolean isReplicatedAddressExist(final Long id) {
        return replicatedAddressRepository.existsById(id);
    }
}
