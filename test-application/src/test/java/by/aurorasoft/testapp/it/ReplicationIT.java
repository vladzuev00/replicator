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
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static java.lang.Long.MAX_VALUE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.Optional.empty;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Import(ReplicationIT.ReplicationBarrier.class)
public class ReplicationIT extends AbstractSpringBootTest {
    private static final String FOREIGN_KEY_VIOLATION_SQL_STATE = "23503";
    private static final String UNIQUE_VIOLATION_SQL_STATE = "23505";

    @Autowired
    private AddressService addressService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ReplicationRetryConsumeProperty retryConsumeProperty;

    @Autowired
    private ReplicationBarrier replicationBarrier;

    @SpyBean
    private ReplicatedAddressRepository replicatedAddressRepository;

    @SpyBean
    private ReplicatedPersonRepository replicatedPersonRepository;

    @Test
    public void addressShouldBeSaved() {
        final Address givenAddress = createAddress("Belarus", "Minsk");

        final Address actual = executeWaitingReplication(() -> addressService.save(givenAddress), 1, 0);
        final Address expected = new Address(1L, givenAddress.getCountry(), givenAddress.getCity());
        assertEquals(expected, actual);

        verifyAddressReplicated(actual);
    }

    @Test
    public void addressShouldNotBeSavedBecauseOfUniqueViolation() {
        final Address givenAddress = createAddress("Russia", "Moscow");

        saveExpectingUniqueViolation(givenAddress);

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void personShouldNotBeSavedBecauseOfForeignKeyViolation() {
        final Person givenPerson = createPerson("Harry", "Potter", "Sergeevich", LocalDate.of(1990, 8, 4), 254L);

        saveExpectingForeignKeyViolation(givenPerson);

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void addressesShouldBeSaved() {
        final Address firstGivenAddress = createAddress("China", "Fuyang");
        final Address secondGivenAddress = createAddress("China", "Hefei");

        final List<Address> actual = executeWaitingReplication(() -> saveAll(firstGivenAddress, secondGivenAddress), 2, 0);
        final List<Address> expected = List.of(
                new Address(1L, firstGivenAddress.getCountry(), firstGivenAddress.getCity()),
                new Address(2L, secondGivenAddress.getCountry(), secondGivenAddress.getCity())
        );
        assertEquals(expected, actual);

        verifyAddressesReplicated(actual);
    }

    @Test
    public void addressesShouldNotBeSavedBecauseOfUniqueViolation() {
        final List<Address> givenAddresses = List.of(
                createAddress("Belarus", "Minsk"),
                createAddress("Russia", "Moscow")
        );

        saveAllExpectingUniqueViolation(givenAddresses);

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeUpdated() {
        final Address givenAddress = new Address(255L, "Belarus", "Minsk");

        final Address actual = executeWaitingReplication(() -> addressService.update(givenAddress), 1, 0);
        assertEquals(givenAddress, actual);

        verifyAddressReplicated(actual);
    }

    @Test
    public void addressShouldNotBeUpdatedBecauseOfUniqueViolation() {
        final Address givenAddress = new Address(256L, "Russia", "Moscow");

        updateExpectingUniqueViolation(givenAddress);

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeUpdatedPartially() {
        final Long givenId = 255L;
        final AddressName givenNewName = new AddressName("Belarus", "Minsk");

        final Address actual = executeWaitingReplication(() -> addressService.updatePartial(givenId, givenNewName), 1, 0);
        final Address expected = new Address(givenId, givenNewName.getCountry(), givenNewName.getCity());
        assertEquals(expected, actual);

        verifyAddressReplicated(actual);
    }

    @Test
    public void addressShouldNotBeUpdatedPartiallyBecauseOfUniqueViolation() {
        final Long givenId = 256L;
        final AddressName givenNewName = new AddressName("Russia", "Moscow");

        updateAddressPartialExpectingUniqueViolation(givenId, givenNewName);

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeDeleted() {
        final Long givenId = 262L;

        executeWaitingReplication(() -> deleteAddress(givenId), 1, 0);

        verifyAddressDeletedWithReplication(givenId);
    }

    @Test
    public void addressShouldNotBeDeletedByNotExistId() {
        final Long givenId = MAX_VALUE;

        executeWaitingReplication(() -> deleteAddress(givenId), 1, 0);

        verifyReplicatedAddressDeleteMethodCall(givenId);
    }

    @Test
    public void addressShouldNotBeDeletedBecauseOfForeignKeyViolation() {
        deleteAddressExpectingForeignKeyViolation(255L);

        verifyNoReplicatedRepositoryMethodCall();
    }

    //TODO: sometimes fails
    @Test
    public void operationsShouldBeExecuted() {
        executeWaitingReplication(
                () -> {
                    saveAll(
                            createAddress("China", "Hong Kong"),
                            createAddress("China", "Anqing"),
                            createAddress("China", "Bozhou")
                    );
                    saveAll(
                            createPerson("Avdifaks", "Kuznetsov", "Vasilievich", LocalDate.of(1995, 7, 2), 1L),
                            createPerson("Vitenka", "Kozar", "Vadimovich", LocalDate.of(1996, 6, 1), 2L),
                            createPerson("Yury", "Sitnikov", "Stepanovich", LocalDate.of(1997, 8, 3), 3L)
                    );
                    addressService.save(createAddress("China", "Huainan"));
                    saveExpectingUniqueViolation(createAddress("China", "Huainan"));
                    saveExpectingUniqueViolation(createAddress("Russia", "Moscow"));
                    addressService.updatePartial(4L, new AddressName("Belarus", "Gomel"));
                    personService.updatePartial(2L, new PersonName("Ivan", "Zuev", "Ivanovich"));
                    personService.delete(MAX_VALUE);
                    addressService.delete(MAX_VALUE);
                    updateAddressPartialExpectingUniqueViolation(256L, new AddressName("Russia", "Moscow"));
                    personService.delete(259L);
                    addressService.delete(257L);
                    personService.update(createPerson(257L, "Alexandr", "Verbitskiy", "Dmitrievich", LocalDate.of(2000, 5, 20), 256L));
                    return empty();
                },
                7,
                7
        );

        verifyDatabase(
                List.of(
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
                        new AddressEntity(263L, "Armenia", "Yerevan"),
                        new AddressEntity(264L, "America", "New York")
                ),
                List.of(
                        createPersonEntity(1L, "Avdifaks", "Kuznetsov", "Vasilievich", LocalDate.of(1995, 7, 2), 1L),
                        createPersonEntity(2L, "Ivan", "Zuev", "Ivanovich", LocalDate.of(1996, 6, 1), 2L),
                        createPersonEntity(3L, "Yury", "Sitnikov", "Stepanovich", LocalDate.of(1997, 8, 3), 3L),
                        createPersonEntity(255L, "Vlad", "Zuev", "Sergeevich", LocalDate.of(2000, 2, 18), 255L),
                        createPersonEntity(256L, "Vasilii", "Dolzhikov", "Borisovich", LocalDate.of(1980, 3, 15), 255L),
                        createPersonEntity(257L, "Alexandr", "Verbitskiy", "Dmitrievich", LocalDate.of(2000, 5, 20), 256L),
                        createPersonEntity(258L, "Pashenka", "Kornev", "Filippovich", LocalDate.of(1995, 4, 23), 256L)
                ),
                List.of(
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
                        new ReplicatedAddressEntity(263L, "Armenia", "Yerevan"),
                        new ReplicatedAddressEntity(265L, "Japan", "Tokyo")
                ),
                List.of(
                        createReplicatedPerson(1L, "Avdifaks", "Kuznetsov", LocalDate.of(1995, 7, 2), 1L),
                        createReplicatedPerson(2L, "Ivan", "Zuev", LocalDate.of(1996, 6, 1), 2L),
                        createReplicatedPerson(3L, "Yury", "Sitnikov", LocalDate.of(1997, 8, 3), 3L),
                        createReplicatedPerson(255L, "Vlad", "Zuev", LocalDate.of(2000, 2, 18), 255L),
                        createReplicatedPerson(256L, "Vasilii", "Dolzhikov", LocalDate.of(1980, 3, 15), 255L),
                        createReplicatedPerson(257L, "Alexandr", "Verbitskiy", LocalDate.of(2000, 5, 20), 256L),
                        createReplicatedPerson(258L, "Pashenka", "Kornev", LocalDate.of(1995, 4, 23), 256L),
                        createReplicatedPerson(260L, "Nikolay", "Radoman", LocalDate.of(2000, 2, 20), 258L)
                )
        );
    }

    @Test
    public void addressShouldBeDeletedButReplicatedAddressShouldNotBecauseOfForeignKeyViolation() {
        final Long givenId = 258L;

        executeWaitingReplication(() -> deleteAddress(givenId), retryConsumeProperty.getMaxAttempts(), 0);

        verifyAddressDeleted(givenId);
        verifyReplicatedAddressExist(givenId);
        verifyReplicatedAddressDeleteMethodCall(givenId, retryConsumeProperty.getMaxAttempts());
    }

    @Test
    public void personShouldBeSavedButNotReplicatedBecauseOfForeignKeyViolation() {
        final Person givenPerson = createPerson("Petr", "Ivanov", "Petrovich", LocalDate.of(2000, 3, 19), 264L);

        final Person actual = executeWaitingReplication(() -> personService.save(givenPerson), 0, retryConsumeProperty.getMaxAttempts());
        final Person expected = new Person(
                1L,
                givenPerson.getName(),
                givenPerson.getSurname(),
                givenPerson.getPatronymic(),
                givenPerson.getBirthDate(),
                givenPerson.getAddress()
        );
        assertEquals(expected, actual);

        verifyReplicationAbsence(actual);
        verifyReplicatedPersonSaveMethodCall(retryConsumeProperty.getMaxAttempts());
    }

    @Test
    public void addressShouldBeSavedButNotReplicatedBecauseOfUniqueConstraint() {
        final Address givenAddress = createAddress("Japan", "Tokyo");

        final Address actual = executeWaitingReplication(() -> addressService.save(givenAddress), 1, 0);
        final Address expected = new Address(1L, givenAddress.getCountry(), givenAddress.getCity());
        assertEquals(expected, actual);

        verifyReplicationAbsence(actual);
        verifyReplicatedAddressSaveMethodCall(1);
    }


    private <R> R executeWaitingReplication(final Supplier<R> operation, final int addressCalls, final int personCalls) {
        replicationBarrier.expect(addressCalls, personCalls);
        final R result = operation.get();
        replicationBarrier.await();
        return result;
    }

    private void verifyAddressReplicated(final Address address) {
        final ReplicatedAddressEntity expected = mapToReplicatedAddress(address);
        verifyReplicatedAddressExist(expected);
    }

    private static ReplicatedAddressEntity mapToReplicatedAddress(final Address address) {
        return new ReplicatedAddressEntity(address.getId(), address.getCountry(), address.getCity());
    }

    private void verifyReplicatedAddressExist(final ReplicatedAddressEntity entity) {
        verifyExist(entity, replicatedAddressRepository, ReplicatedAddressEntityUtil::checkEquals);
    }

    private void verifyAddressesReplicated(final List<Address> addresses) {
        final List<ReplicatedAddressEntity> expected = mapToReplicatedAddresses(addresses);
        verifyReplicatedAddressesExist(expected);
    }

    private static List<ReplicatedAddressEntity> mapToReplicatedAddresses(final List<Address> addresses) {
        return addresses.stream()
                .map(ReplicationIT::mapToReplicatedAddress)
                .toList();
    }

    private void verifyReplicatedAddressesExist(final List<ReplicatedAddressEntity> entities) {
        verifyExist(entities, replicatedAddressRepository, ReplicatedAddressEntityUtil::checkEquals);
    }

    private static <ID extends Comparable<ID>, E extends AbstractEntity<ID>> void verifyExist(
            final E entity,
            final JpaRepository<E, ID> repository,
            final BiConsumer<E, E> equalChecker
    ) {
        verifyExist(singletonList(entity), repository, equalChecker);
    }

    private static <ID extends Comparable<ID>, E extends AbstractEntity<ID>> void verifyExist(
            final List<E> entities,
            final JpaRepository<E, ID> repository,
            final BiConsumer<E, E> equalChecker
    ) {
        final List<ID> ids = mapToIds(entities);
        final List<E> actual = findAllByIdOrderById(ids, repository);
        checkEquals(entities, actual, equalChecker);
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

    //TODO: stop
    private boolean isAddressDeleted(final Long id) {
        return !addressService.isExist(id) && !replicatedAddressRepository.existsById(id);
    }

    private static void verifyUniqueConstraintViolation(final DataIntegrityViolationException exception) {
        assertEquals(UNIQUE_VIOLATION_SQL_STATE, getSqlState(exception));
    }

    private static String getSqlState(final DataIntegrityViolationException exception) {
        return ((SQLException) getRootCause(exception)).getSQLState();
    }

    private Optional<Void> saveExpectingUniqueViolation(final Address address) {
        executeExpectingUniqueConstraintViolation(() -> addressService.save(address));
        return empty();
    }

    private void saveExpectingForeignKeyViolation(final Person person) {
        executeExpectingForeignKeyConstraintViolation(() -> personService.save(person));
    }

    private static Optional<Void> saveExpectingUniqueConstraintViolation(final AddressService addressService, final Address address) {
        executeExpectingUniqueConstraintViolation(() -> addressService.save(address));
        return empty();
    }

    private Optional<Void> saveAllExpectingUniqueViolation(final Collection<Address> addresses) {
        executeExpectingUniqueConstraintViolation(() -> addressService.saveAll(addresses));
        return empty();
    }

    private Optional<Void> updateExpectingUniqueViolation(final Address address) {
        executeExpectingUniqueConstraintViolation(() -> addressService.update(address));
        return empty();
    }

    private Optional<Void> updateAddressPartialExpectingUniqueViolation(final Long id, final Object partial) {
        executeExpectingUniqueConstraintViolation(() -> addressService.updatePartial(id, partial));
        return empty();
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

    private static void executeExpectingForeignKeyConstraintViolation(final Runnable task) {
        boolean exceptionArisen;
        try {
            task.run();
            exceptionArisen = false;
        } catch (final DataIntegrityViolationException exception) {
            exceptionArisen = true;
            assertEquals(getSqlState(exception), FOREIGN_KEY_VIOLATION_SQL_STATE);
        }
        assertTrue(exceptionArisen);
    }

    private Optional<Void> deleteAddressExpectingForeignKeyViolation(final Long id) {
        executeExpectingForeignKeyViolation(() -> addressService.delete(id));
        return empty();
    }

    private Optional<Void> executeExpectingForeignKeyViolation(final Runnable operation) {
        executeExpectingForeignKeyConstraintViolation(operation);
        return empty();
    }

    @SuppressWarnings("SameParameterValue")
    private void verifyReplicatedPersonsCount(final long expected) {
        assertEquals(expected, countReplicatedPersons());
    }

    private void verifyAddressesCount(final long expected) {
        assertEquals(expected, countAddresses());
    }

    private void verifyReplicatedAddressesCount(final long expected) {
        assertEquals(expected, countReplicatedAddresses());
    }

    private long countAddresses() {
        return queryForLong("SELECT COUNT(e) FROM AddressEntity e");
    }

    private long countReplicatedAddresses() {
        return queryForLong("SELECT COUNT(e) FROM ReplicatedAddressEntity e");
    }

    private long queryForLong(final String hqlQuery) {
        return entityManager.createQuery(hqlQuery, Long.class).getSingleResult();
    }

    private long countReplicatedPersons() {
        return entityManager.createQuery("SELECT COUNT(e) FROM ReplicatedPersonEntity e", Long.class).getSingleResult();
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
        return createPerson(null, name, surname, patronymic, birthDate, addressId);
    }

    @SuppressWarnings("SameParameterValue")
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

    private static Person createPerson(final Long id,
                                       final String name,
                                       final String surname,
                                       final String patronymic,
                                       final LocalDate birthDate,
                                       final Long addressId) {
        return new Person(id, name, surname, patronymic, birthDate, createAddress(addressId));
    }

    private static ReplicatedPersonEntity createReplicatedPerson(final Long id,
                                                                 final String name,
                                                                 final String surname,
                                                                 final LocalDate birthDate,
                                                                 final Long addressId) {
        return new ReplicatedPersonEntity(id, name, surname, birthDate, createReplicatedAddressEntity(addressId));
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
        return new PersonEntity(id, name, surname, patronymic, birthDate, createAddressEntity(addressId));
    }

    private static AddressEntity createAddressEntity(final Long id) {
        return AddressEntity.builder()
                .id(id)
                .build();
    }

    private List<Address> saveAll(final Address... addresses) {
        return saveAll(addressService, addresses);
    }

    private static List<Address> saveAddresses(final AddressService service, final Address... addresses) {
        return saveAll(service, addresses);
    }

    private void saveAll(final Person... persons) {
        saveAll(personService, persons);
    }

    @SafeVarargs
    private static <T extends AbstractDto<?>> List<T> saveAll(final AbsServiceCRUD<?, ?, T, ?> service, final T... dtos) {
        return service.saveAll(asList(dtos));
    }

    private Optional<Void> deleteAddress(final Long id) {
        addressService.delete(id);
        return empty();
    }

    private void verifyReplicatedAddressExist(final Long id) {
        assertTrue(replicatedAddressRepository.existsById(id));
    }

    private void verifyNoReplicatedRepositoryMethodCall() {
        verifyNoInteractions(replicatedAddressRepository);
        verifyNoInteractions(replicatedPersonRepository);
    }

    private void verifyAddressDeletedWithReplication(final Long id) {
        assertTrue(isAddressDeleted(id));
    }

    private void verifyReplicatedAddressDeleteMethodCall(final Long id) {
        verifyDeleteReplicatedAddressMethodCalls(id, 1);
    }

    private void verifyDeleteReplicatedAddressMethodCalls(final Long id, final int times) {
        verify(replicatedAddressRepository, times(times)).deleteById(eq(id));
    }

    private void verifyReplicatedAddressDeleteMethodCall(final Long id, final int times) {
        verify(replicatedAddressRepository, times(times)).deleteById(eq(id));
    }

    private void verifyReplicatedPersonSaveMethodCall(final int times) {
        verify(replicatedPersonRepository, times(times)).save(any(ReplicatedPersonEntity.class));
    }

    private void verifyReplicatedAddressSaveMethodCall(final int times) {
        verify(replicatedAddressRepository, times(times)).save(any(ReplicatedAddressEntity.class));
    }

    private void verifyDatabase(final List<AddressEntity> expectedAddresses,
                                final List<PersonEntity> expectedPersons,
                                final List<ReplicatedAddressEntity> expectedReplicatedAddresses,
                                final List<ReplicatedPersonEntity> expectedReplicatedPersons) {
        verifyAddresses(expectedAddresses);
        verifyPersons(expectedPersons);
        verifyReplicatedAddresses(expectedReplicatedAddresses);
        verifyReplicatedPersons(expectedReplicatedPersons);
    }

    private void verifyAddresses(final List<AddressEntity> expected) {
        final List<AddressEntity> actual = findAddressesOrderedById();
        checkEqualsAddresses(expected, actual);
    }

    private void verifyPersons(final List<PersonEntity> expected) {
        final List<PersonEntity> actual = findPersonsOrderedById();
        checkEqualsPersons(expected, actual);
    }

    private void verifyReplicatedAddresses(final List<ReplicatedAddressEntity> expected) {
        final List<ReplicatedAddressEntity> actual = findReplicatedAddressesOrderedById();
        checkEqualsReplicatedAddresses(expected, actual);
    }

    private void verifyReplicatedPersons(final List<ReplicatedPersonEntity> expected) {
        final List<ReplicatedPersonEntity> actual = findReplicatedPersonsOrderedById();
        checkEqualsReplicatedPersons(expected, actual);
    }

    private void verifyAddressDeleted(final Long id) {
        assertFalse(addressService.isExist(id));
    }

    private void verifyReplicationAbsence(final Person person) {
        assertFalse(replicatedPersonRepository.existsById(person.getId()));
    }

    private void verifyReplicationAbsence(final Address address) {
        assertFalse(replicatedAddressRepository.existsById(address.getId()));
    }

    @Aspect
    @Component
    public static class ReplicationBarrier {
        private volatile CountDownLatch addressLatch;
        private volatile CountDownLatch personLatch;

        public final void expect(final int addressCalls, final int personCalls) {
            addressLatch = new CountDownLatch(addressCalls);
            personLatch = new CountDownLatch(personCalls);
        }

        @Around("replicatedAddressRepository()")
        public Object onAddressMethodCall(final ProceedingJoinPoint joinPoint)
                throws Throwable {
            return onMethodCall(joinPoint, addressLatch);
        }

        @Around("replicatedPersonRepository()")
        public Object onPersonMethodCall(final ProceedingJoinPoint joinPoint)
                throws Throwable {
            return onMethodCall(joinPoint, personLatch);
        }

        public final void await() {
            try {
                addressLatch.await();
                personLatch.await();
            } catch (final InterruptedException cause) {
                throw new IllegalStateException(cause);
            }
        }

        private static Object onMethodCall(final ProceedingJoinPoint joinPoint, final CountDownLatch latch)
                throws Throwable {
            try {
                return joinPoint.proceed();
            } finally {
                latch.countDown();
            }
        }

        @Pointcut("target(by.aurorasoft.testapp.crud.repository.ReplicatedAddressRepository)")
        private void replicatedAddressRepository() {

        }

        @Pointcut("target(by.aurorasoft.testapp.crud.repository.ReplicatedPersonRepository)")
        private void replicatedPersonRepository() {

        }
    }
}
