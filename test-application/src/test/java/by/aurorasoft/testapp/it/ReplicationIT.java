package by.aurorasoft.testapp.it;

import by.aurorasoft.testapp.base.AbstractSpringBootTest;
import by.aurorasoft.testapp.crud.dto.Address;
import by.aurorasoft.testapp.crud.dto.Person;
import by.aurorasoft.testapp.crud.entity.AbstractEntity;
import by.aurorasoft.testapp.crud.entity.ReplicatedAddressEntity;
import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import by.aurorasoft.testapp.crud.repository.ReplicatedAddressRepository;
import by.aurorasoft.testapp.crud.repository.ReplicatedPersonRepository;
import by.aurorasoft.testapp.crud.service.AddressService;
import by.aurorasoft.testapp.crud.service.PersonService;
import by.aurorasoft.testapp.model.AddressName;
import by.aurorasoft.testapp.model.PersonName;
import by.aurorasoft.testapp.util.ReplicatedAddressEntityUtil;
import by.aurorasoft.testapp.util.ReplicatedPersonEntityUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Thread.currentThread;
import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

//TODO: do test with a lot of operations
@DirtiesContext
@Transactional(propagation = NOT_SUPPORTED)
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
    private ReplicatedPersonRepository replicatedPersonRepository;

    //TODO: run tests many times, replicated person can save before replicated address
    @Test
    public void personAndAddressShouldBeSaved() {
        final String givenAddressCountry = "Belarus";
        final String givenAddressCity = "Minsk";

        final String givenPersonName = "Vlad";
        final String givenPersonSurname = "Zuev";
        final String givenPersonPatronymic = "Sergeevich";
        final LocalDate givenPersonBirthDate = LocalDate.of(2000, 2, 18);

        final Address givenAddress = Address.builder()
                .country(givenAddressCountry)
                .city(givenAddressCity)
                .build();
        final Address actualSavedAddress = addressService.save(givenAddress);

        final Person givenPerson = Person.builder()
                .name(givenPersonName)
                .surname(givenPersonSurname)
                .patronymic(givenPersonPatronymic)
                .birthDate(givenPersonBirthDate)
                .address(actualSavedAddress)
                .build();
        final Person actualSavedPerson = personService.save(givenPerson);

        waitReplicating();

        final Long expectedSavedAddressId = 1L;
        final Address expectedSavedAddress = new Address(expectedSavedAddressId, givenAddressCountry, givenAddressCity);
        assertEquals(expectedSavedAddress, actualSavedAddress);

        final Long expectedSavedPersonId = 1L;
        final Person expectedSavedPerson = Person.builder()
                .id(expectedSavedPersonId)
                .name(givenPersonName)
                .surname(givenPersonSurname)
                .patronymic(givenPersonPatronymic)
                .birthDate(givenPersonBirthDate)
                .address(expectedSavedAddress)
                .build();
        assertEquals(expectedSavedPerson, actualSavedPerson);

        final ReplicatedAddressEntity expectedReplicatedAddress = ReplicatedAddressEntity.builder()
                .id(expectedSavedAddressId)
                .country(givenAddressCountry)
                .city(givenAddressCity)
                .build();
        verifySave(expectedReplicatedAddress);

        final ReplicatedPersonEntity expectedReplicatedPerson = ReplicatedPersonEntity.builder()
                .id(expectedSavedPersonId)
                .name(givenPersonName)
                .surname(givenPersonSurname)
                .birthDate(givenPersonBirthDate)
                .address(expectedReplicatedAddress)
                .build();
        verifySave(expectedReplicatedPerson);
    }

    @Test
    @Sql("classpath:sql-scripts/replication/it/insert-person-and-addresses.sql")
    public void addressShouldNotBeSavedBecauseOfViolationUniqueConstraint() {
        final Address givenAddress = Address.builder()
                .country("Belarus")
                .city("Minsk")
                .build();

        saveExpectingUniqueConstraintViolation(givenAddress);
        waitReplicating();
        verifyReplicatedAddressesCount(2);
    }

    //TODO: run tests many times, replicated person can save before replicated address
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

        final Address givenAddress = Address.builder()
                .country(givenAddressCountry)
                .city(givenAddressCity)
                .build();
        final Address actualSavedAddress = addressService.save(givenAddress);

        final List<Person> givenPersons = List.of(
                Person.builder()
                        .name(givenFirstPersonName)
                        .surname(givenFirstPersonSurname)
                        .patronymic(givenFirstPersonPatronymic)
                        .birthDate(givenFirstPersonBirthDate)
                        .address(actualSavedAddress)
                        .build(),
                Person.builder()
                        .name(givenSecondPersonName)
                        .surname(givenSecondPersonSurname)
                        .patronymic(givenSecondPersonPatronymic)
                        .birthDate(givenSecondPersonBirthDate)
                        .address(actualSavedAddress)
                        .build()
        );
        final List<Person> actualSavedPersons = personService.saveAll(givenPersons);

        waitReplicating();

        final Long expectedSavedAddressId = 1L;
        final Address expectedSavedAddress = new Address(expectedSavedAddressId, givenAddressCountry, givenAddressCity);
        assertEquals(expectedSavedAddress, actualSavedAddress);

        final Long expectedFirstSavedPersonId = 1L;
        final Long expectedSecondSavedPersonId = 2L;
        final List<Person> expectedSavedPersons = List.of(
                Person.builder()
                        .id(expectedFirstSavedPersonId)
                        .name(givenFirstPersonName)
                        .surname(givenFirstPersonSurname)
                        .patronymic(givenFirstPersonPatronymic)
                        .birthDate(givenFirstPersonBirthDate)
                        .address(expectedSavedAddress)
                        .build(),
                Person.builder()
                        .id(expectedSecondSavedPersonId)
                        .name(givenSecondPersonName)
                        .surname(givenSecondPersonSurname)
                        .patronymic(givenSecondPersonPatronymic)
                        .birthDate(givenSecondPersonBirthDate)
                        .address(expectedSavedAddress)
                        .build()
        );
        assertEquals(expectedSavedPersons, actualSavedPersons);

        final ReplicatedAddressEntity expectedReplicatedAddress = ReplicatedAddressEntity.builder()
                .id(expectedSavedAddressId)
                .country(givenAddressCountry)
                .city(givenAddressCity)
                .build();
        verifySave(expectedReplicatedAddress);

        final List<ReplicatedPersonEntity> expectedReplicatedPersons = List.of(
                ReplicatedPersonEntity.builder()
                        .id(expectedFirstSavedPersonId)
                        .name(givenFirstPersonName)
                        .surname(givenFirstPersonSurname)
                        .birthDate(givenFirstPersonBirthDate)
                        .address(expectedReplicatedAddress)
                        .build(),
                ReplicatedPersonEntity.builder()
                        .id(expectedSecondSavedPersonId)
                        .name(givenSecondPersonName)
                        .surname(givenSecondPersonSurname)
                        .birthDate(givenSecondPersonBirthDate)
                        .address(expectedReplicatedAddress)
                        .build()
        );
        verifySave(expectedReplicatedPersons);
    }

    @Test
    @Sql("classpath:sql-scripts/replication/it/insert-person-and-addresses.sql")
    public void addressesShouldNotBeSavedBecauseOfViolationUniqueConstraint() {
        final List<Address> givenAddresses = List.of(
                Address.builder()
                        .country("Russia")
                        .city("Moscow")
                        .build(),
                Address.builder()
                        .country("Belarus")
                        .city("Minsk")
                        .build()
        );

        saveAllExpectingUniqueConstraintViolation(givenAddresses);
        waitReplicating();
        verifyReplicatedAddressesCount(1);
    }

    @Test
    @Sql("classpath:sql-scripts/replication/it/insert-person-and-addresses.sql")
    public void personShouldBeUpdated() {
        final Long givenId = 255L;
        final String givenNewName = "Ivan";
        final String givenNewSurname = "Ivanov";
        final String givenNewPatronymic = "Ivanovich";
        final LocalDate givenNewBirthDate = LocalDate.of(2000, 2, 19);

        final Long givenAddressId = 255L;
        final Address givenAddress = new Address(givenAddressId, "Belarus", "Minsk");

        final Person givenNewPerson = Person.builder()
                .id(givenId)
                .name(givenNewName)
                .surname(givenNewSurname)
                .patronymic(givenNewPatronymic)
                .birthDate(givenNewBirthDate)
                .address(givenAddress)
                .build();

        final Person actualUpdatedPerson = personService.update(givenNewPerson);

        waitReplicating();

        assertEquals(givenNewPerson, actualUpdatedPerson);

        final var expectedReplicatedAddress = entityManager.getReference(ReplicatedAddressEntity.class, givenAddressId);
        final ReplicatedPersonEntity expectedReplicatedPerson = ReplicatedPersonEntity.builder()
                .id(givenId)
                .name(givenNewName)
                .surname(givenNewSurname)
                .birthDate(givenNewBirthDate)
                .address(expectedReplicatedAddress)
                .build();
        verifySave(expectedReplicatedPerson);
    }

    @Test
    @Sql("classpath:sql-scripts/replication/it/insert-person-and-addresses.sql")
    public void addressShouldNotBeUpdatedBecauseOfViolationUniqueConstraint() {
        final Address givenAddress = Address.builder()
                .id(256L)
                .country("Belarus")
                .city("Minsk")
                .build();

        final List<ReplicatedAddressEntity> replicatedAddressesBeforeUpdate = findReplicatedAddresses();
        updateExpectingUniqueConstraintViolation(givenAddress);
        waitReplicating();
        final List<ReplicatedAddressEntity> replicatedAddressesAfterUpdate = findReplicatedAddresses();
        checkEquals(replicatedAddressesBeforeUpdate, replicatedAddressesAfterUpdate);
    }

    @Test
    @Sql("classpath:sql-scripts/replication/it/insert-person-and-addresses.sql")
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
        final Address expectedAddress = new Address(expectedAddressId, "Belarus", "Minsk");

        final Person expectedUpdatedPerson = Person.builder()
                .id(givenId)
                .name(givenNewName)
                .surname(givenNewSurname)
                .patronymic(givenNewPatronymic)
                .birthDate(expectedBirthDate)
                .address(expectedAddress)
                .build();
        assertEquals(expectedUpdatedPerson, actualUpdatedPerson);

        final var expectedReplicatedAddress = entityManager.getReference(ReplicatedAddressEntity.class, expectedAddressId);
        final ReplicatedPersonEntity expectedReplicatedPerson = ReplicatedPersonEntity.builder()
                .id(givenId)
                .name(givenNewName)
                .surname(givenNewSurname)
                .birthDate(expectedBirthDate)
                .address(expectedReplicatedAddress)
                .build();
        verifySave(expectedReplicatedPerson);
    }

    @Test
    @Sql("classpath:sql-scripts/replication/it/insert-person-and-addresses.sql")
    public void addressShouldNotBeUpdatedPartiallyBecauseOfViolationUniqueConstraint() {
        final Long givenId = 256L;
        final AddressName givenAddressName = new AddressName("Belarus", "Minsk");

        final List<ReplicatedAddressEntity> replicatedAddressesBeforeUpdate = findReplicatedAddresses();
        updateAddressPartialExpectingUniqueConstraintViolation(givenId, givenAddressName);
        waitReplicating();
        final List<ReplicatedAddressEntity> replicatedAddressesAfterUpdate = findReplicatedAddresses();
        checkEquals(replicatedAddressesBeforeUpdate, replicatedAddressesAfterUpdate);
    }

    @Test
    @Sql("classpath:sql-scripts/replication/it/insert-person-and-addresses.sql")
    public void personShouldBeDelete() {
        final Long givenId = 255L;

        personService.delete(givenId);

        waitReplicating();

        assertTrue(isPersonDeleted(givenId));
    }

    @Test
    @Sql("classpath:sql-scripts/replication/it/insert-person-and-addresses.sql")
    public void personShouldNotBeDeletedByNotExistId() {
        personService.delete(MAX_VALUE);
        waitReplicating();
        verifyReplicatedPersonsCount(1);
    }

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

    private static <ID, E extends AbstractEntity<ID>> void verifySave(final E expected,
                                                                      final JpaRepository<E, ID> repository,
                                                                      final BiConsumer<E, E> equalChecker) {
        verifySave(singletonList(expected), repository, equalChecker);
    }

    private static <ID, E extends AbstractEntity<ID>> void verifySave(final List<E> expected,
                                                                      final JpaRepository<E, ID> repository,
                                                                      final BiConsumer<E, E> equalChecker) {
        final List<ID> ids = mapToIds(expected);
        final List<E> actual = repository.findAllById(ids);
        checkEquals(expected, actual, equalChecker);
    }

    private static <ID> List<ID> mapToIds(final List<? extends AbstractEntity<ID>> entities) {
        return entities.stream()
                .map(AbstractEntity::getId)
                .toList();
    }

    private static <E extends AbstractEntity<?>> void checkEquals(final List<E> expected,
                                                                  final List<E> actual,
                                                                  final BiConsumer<E, E> equalChecker) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> equalChecker.accept(expected.get(i), actual.get(i)));
    }

    private static void checkEquals(final List<ReplicatedAddressEntity> expected,
                                    final List<ReplicatedAddressEntity> actual) {
        checkEquals(expected, actual, ReplicatedAddressEntityUtil::checkEquals);
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

    private void verifyReplicatedAddressesCount(final long expected) {
        assertEquals(expected, countReplicatedAddresses());
    }

    private void verifyReplicatedPersonsCount(final long expected) {
        assertEquals(expected, countReplicatedPersons());
    }

    private long countReplicatedAddresses() {
        return entityManager.createQuery("SELECT COUNT(e) FROM ReplicatedAddressEntity e", Long.class)
                .getSingleResult();
    }

    private long countReplicatedPersons() {
        return entityManager.createQuery("SELECT COUNT(e) FROM ReplicatedPersonEntity e", Long.class)
                .getSingleResult();
    }

    private List<ReplicatedAddressEntity> findReplicatedAddresses() {
        return entityManager.createQuery("SELECT e FROM ReplicatedAddressEntity e", ReplicatedAddressEntity.class)
                .getResultList();
    }
}
