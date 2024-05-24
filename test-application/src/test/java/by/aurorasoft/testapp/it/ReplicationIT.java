package by.aurorasoft.testapp.it;

import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
import by.aurorasoft.testapp.base.AbstractSpringBootTest;
import by.aurorasoft.testapp.crud.dto.Address;
import by.aurorasoft.testapp.crud.dto.Person;
import by.aurorasoft.testapp.crud.v2.entity.*;
import by.aurorasoft.testapp.crud.v2.repository.ReplicatedAddressRepository;
import by.aurorasoft.testapp.crud.v2.repository.ReplicatedPersonRepository;
import by.aurorasoft.testapp.crud.v2.service.AddressService;
import by.aurorasoft.testapp.crud.v2.service.PersonService;
import by.aurorasoft.testapp.model.AddressName;
import by.aurorasoft.testapp.model.PersonAddress;
import by.aurorasoft.testapp.model.PersonName;
import by.aurorasoft.testapp.util.AddressEntityUtil;
import by.aurorasoft.testapp.util.PersonEntityUtil;
import by.aurorasoft.testapp.util.ReplicatedAddressEntityUtil;
import by.aurorasoft.testapp.util.ReplicatedPersonEntityUtil;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static by.aurorasoft.testapp.util.EntityUtil.checkEquals;
import static by.aurorasoft.testapp.util.IdUtil.mapToIds;
import static java.lang.Long.MAX_VALUE;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Import(ReplicationIT.ReplicationBarrier.class)
public final class ReplicationIT extends AbstractSpringBootTest {
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

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void addressShouldBeSaved() {
        final Address givenAddress = createAddress("Belarus", "Minsk");

        final Address actual = executeWaitingReplication(() -> addressService.save(givenAddress), 1, 0, false);
        final Address expected = new Address(1L, givenAddress.getCountry(), givenAddress.getCity());
        assertEquals(expected, actual);

        verifyAddressReplication(actual);
    }

    @Test
    public void addressShouldNotBeSavedBecauseOfUniqueViolation() {
        final Address givenAddress = createAddress("Russia", "Moscow");

        executeExpectingUniqueViolation(() -> addressService.save(givenAddress));

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void personShouldNotBeSavedBecauseOfForeignKeyViolation() {
        final Person givenPerson = createPerson("Harry", "Potter", "Sergeevich", LocalDate.of(1990, 8, 4), 254L);

        executeExpectingForeignKeyViolation(() -> personService.save(givenPerson));

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void addressesShouldBeSaved() {
        final Address firstGivenAddress = createAddress("China", "Fuyang");
        final Address secondGivenAddress = createAddress("China", "Hefei");
        final List<Address> givenAddresses = List.of(firstGivenAddress, secondGivenAddress);

        final List<Address> actual = executeWaitingReplication(() -> addressService.saveAll(givenAddresses), givenAddresses.size(), 0, false);
        final List<Address> expected = List.of(
                new Address(1L, firstGivenAddress.getCountry(), firstGivenAddress.getCity()),
                new Address(2L, secondGivenAddress.getCountry(), secondGivenAddress.getCity())
        );
        assertEquals(expected, actual);

        verifyAddressReplications(actual);
    }

    @Test
    public void addressesShouldNotBeSavedBecauseOfUniqueViolation() {
        final List<Address> givenAddresses = List.of(
                createAddress("Belarus", "Minsk"),
                createAddress("Russia", "Moscow")
        );

        executeExpectingUniqueViolation(() -> addressService.saveAll(givenAddresses));

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void personsShouldNotBeSavedBecauseOfForeignKeyViolation() {
        final List<Person> givenPersons = List.of(
                createPerson("Avdifaks", "Kuznetsov", "Vasilievich", LocalDate.of(1995, 7, 2), 255L),
                createPerson("Harry", "Potter", "Sergeevich", LocalDate.of(1990, 8, 4), 254L)
        );

        executeExpectingForeignKeyViolation(() -> personService.saveAll(givenPersons));

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeUpdated() {
        final Address givenAddress = new Address(255L, "Belarus", "Minsk");

        final Address actual = executeWaitingReplication(() -> addressService.update(givenAddress), 1, 0, false);
        assertEquals(givenAddress, actual);

        verifyAddressReplication(actual);
    }

    @Test
    public void addressShouldNotBeUpdatedBecauseOfUniqueViolation() {
        final Address givenAddress = new Address(256L, "Russia", "Moscow");

        executeExpectingUniqueViolation(() -> addressService.update(givenAddress));

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void personShouldNotBeUpdatedBecauseOfNoSuchAddress() {
        final Person givenPerson = createPerson(255L, "Vlad", "Zuev", "Sergeevich", LocalDate.of(2000, 2, 18), 254L);

        executeExpectingNoSuchEntityException(() -> personService.update(givenPerson));

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeUpdatedPartially() {
        final Long givenId = 255L;
        final AddressName givenNewName = new AddressName("Belarus", "Minsk");

        final Address actual = executeWaitingReplication(() -> addressService.updatePartial(givenId, givenNewName), 1, 0, false);
        final Address expected = new Address(givenId, givenNewName.getCountry(), givenNewName.getCity());
        assertEquals(expected, actual);

        verifyAddressReplication(actual);
    }

    @Test
    public void addressShouldNotBeUpdatedPartiallyBecauseOfUniqueViolation() {
        final Long givenId = 256L;
        final AddressName givenNewName = new AddressName("Russia", "Moscow");

        executeExpectingUniqueViolation(() -> addressService.updatePartial(givenId, givenNewName));

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void personShouldNotBeUpdatedPartiallyBecauseOfNoSuchAddress() {
        final Long givenId = 255L;
        final PersonAddress givenNewAddress = new PersonAddress(createAddress(254L));

        executeExpectingNoSuchEntityException(() -> personService.updatePartial(givenId, givenNewAddress));

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeDeleted() {
        final Long givenId = 262L;

        executeWaitingReplication(() -> deleteAddress(givenId), 1, 0, false);

        assertTrue(isAddressDeletedWithReplication(givenId));
    }

    @Test
    public void addressShouldNotBeDeletedByNotExistId() {
        final Long givenId = MAX_VALUE;

        executeWaitingReplication(() -> deleteAddress(givenId), 1, 0, true);

        verifyAttemptDeleteReplicatedAddress(givenId);
    }

    @Test
    public void addressShouldNotBeDeletedBecauseOfForeignKeyViolation() {
        final Long givenId = 255L;

        executeExpectingForeignKeyViolation(() -> addressService.delete(givenId));

        verifyNoReplicatedRepositoryMethodCall();
    }

    @Test
    public void operationsShouldBeExecuted() {
        executeWaitingReplication(
                () -> {
                    addressService.saveAll(
                            List.of(
                                    createAddress("China", "Hong Kong"),
                                    createAddress("China", "Anqing"),
                                    createAddress("China", "Bozhou")
                            )
                    );
                    personService.saveAll(
                            List.of(
                                    createPerson("Avdifaks", "Kuznetsov", "Vasilievich", LocalDate.of(1995, 7, 2), 1L),
                                    createPerson("Vitenka", "Kozar", "Vadimovich", LocalDate.of(1996, 6, 1), 2L),
                                    createPerson("Yury", "Sitnikov", "Stepanovich", LocalDate.of(1997, 8, 3), 3L)
                            )
                    );
                    addressService.save(createAddress("China", "Huainan"));
                    executeExpectingUniqueViolation(() -> addressService.save(createAddress("China", "Huainan")));
                    executeExpectingUniqueViolation(() -> addressService.save(createAddress("Russia", "Moscow")));
                    addressService.updatePartial(4L, new AddressName("Belarus", "Gomel"));
                    personService.updatePartial(2L, new PersonName("Ivan", "Zuev", "Ivanovich"));
                    personService.delete(MAX_VALUE);
                    addressService.delete(MAX_VALUE);
                    executeExpectingUniqueViolation(() -> addressService.updatePartial(256L, new AddressName("Russia", "Moscow")));
                    personService.delete(259L);
                    addressService.delete(257L);
                    personService.update(createPerson(257L, "Alexandr", "Verbitskiy", "Dmitrievich", LocalDate.of(2000, 5, 20), 256L));
                    return empty();
                },
                7,
                7,
                false
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

        executeWaitingReplication(() -> deleteAddress(givenId), retryConsumeProperty.getMaxAttempts(), 0, true);

        assertFalse(addressService.isExist(givenId));
        assertTrue(replicatedAddressRepository.existsById(givenId));
        verifyMaxAttemptDeleteReplicatedAddress(givenId);
    }

    @Test
    public void personShouldBeSavedButNotReplicatedBecauseOfForeignKeyViolation() {
        final Person givenPerson = createPerson("Petr", "Ivanov", "Petrovich", LocalDate.of(2000, 3, 19), 264L);

        final Person actual = executeWaitingReplication(() -> personService.save(givenPerson), 0, retryConsumeProperty.getMaxAttempts(), true);
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
        verifyMaxAttemptSaveReplicatedPerson();
    }

    @Test
    public void addressShouldBeSavedButNotReplicatedBecauseOfUniqueConstraint() {
        final Address givenAddress = createAddress("Japan", "Tokyo");

        final Address actual = executeWaitingReplication(() -> addressService.save(givenAddress), 1, 0, true);
        final Address expected = new Address(1L, givenAddress.getCountry(), givenAddress.getCity());
        assertEquals(expected, actual);

        verifyReplicationAbsence(actual);
        verifyAttemptSaveReplicatedAddress();
    }

    @Test
    public void addressShouldNotBeDeletedBecauseOfTransactionRollBacked() {
        final Long givenId = 262L;

        deleteAddressInRollBackedTransaction(givenId);

        assertTrue(isAddressExistWithReplication(givenId));
    }

    private <R> R executeWaitingReplication(final Supplier<R> operation,
                                            final int addressCalls,
                                            final int personCalls,
                                            final boolean failedCallsCounted) {
        replicationBarrier.expect(addressCalls, personCalls, failedCallsCounted);
        final R result = operation.get();
        replicationBarrier.await();
        return result;
    }

    private void verifyAddressReplication(final Address address) {
        verifyAddressReplications(singletonList(address));
    }

    private void verifyAddressReplications(final List<Address> addresses) {
        final List<Long> ids = mapToIds(addresses);
        final List<ReplicatedAddressEntity> actual = findReplicatedAddressesOrderedById(ids);
        final List<ReplicatedAddressEntity> expected = mapToReplicatedAddresses(addresses);
        ReplicatedAddressEntityUtil.checkEquals(expected, actual);
    }

    private List<ReplicatedAddressEntity> findReplicatedAddressesOrderedById(final List<Long> ids) {
        return entityManager
                .createQuery("SELECT e FROM ReplicatedAddressEntity e WHERE e.id IN :ids ORDER BY e.id", ReplicatedAddressEntity.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    private static List<ReplicatedAddressEntity> mapToReplicatedAddresses(final List<Address> addresses) {
        return addresses.stream()
                .map(ReplicationIT::mapToReplicatedAddress)
                .toList();
    }

    private static ReplicatedAddressEntity mapToReplicatedAddress(final Address address) {
        return new ReplicatedAddressEntity(address.getId(), address.getCountry(), address.getCity());
    }

    private static void executeExpectingUniqueViolation(final Runnable task) {
        executeExpectingConstraintViolation(task, UNIQUE_VIOLATION_SQL_STATE);
    }

    private static void executeExpectingForeignKeyViolation(final Runnable task) {
        executeExpectingConstraintViolation(task, FOREIGN_KEY_VIOLATION_SQL_STATE);
    }

    private static void executeExpectingConstraintViolation(final Runnable task, final String expectedSqlState) {
        executeExpectingMatchingException(task, exception -> Objects.equals(expectedSqlState, getSqlState(exception)));
    }

    private static void executeExpectingNoSuchEntityException(final Runnable task) {
        executeExpectingMatchingException(task, exception -> getRootCause(exception).getClass() == EntityNotFoundException.class);
    }

    private static void executeExpectingMatchingException(final Runnable task, final Predicate<Throwable> predicate) {
        boolean exceptionArisen;
        try {
            task.run();
            exceptionArisen = false;
        } catch (final Throwable exception) {
            exceptionArisen = true;
            assertTrue(predicate.test(exception));
        }
        assertTrue(exceptionArisen);
    }

    private static String getSqlState(final Throwable exception) {
        return ((SQLException) getRootCause(exception)).getSQLState();
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
        verifyEntities(
                expected,
                "SELECT e FROM AddressEntity e ORDER BY e.id",
                AddressEntity.class,
                AddressEntityUtil::checkEquals
        );
    }

    private void verifyPersons(final List<PersonEntity> expected) {
        verifyEntities(
                expected,
                "SELECT e FROM PersonEntity e ORDER BY e.id",
                PersonEntity.class,
                PersonEntityUtil::checkEquals
        );
    }

    private void verifyReplicatedAddresses(final List<ReplicatedAddressEntity> expected) {
        verifyEntities(
                expected,
                "SELECT e FROM ReplicatedAddressEntity e ORDER BY e.id",
                ReplicatedAddressEntity.class,
                ReplicatedAddressEntityUtil::checkEquals
        );
    }

    private void verifyReplicatedPersons(final List<ReplicatedPersonEntity> expected) {
        verifyEntities(
                expected,
                "SELECT e FROM ReplicatedPersonEntity e ORDER BY e.id",
                ReplicatedPersonEntity.class,
                ReplicatedPersonEntityUtil::checkEquals
        );
    }

    private <E extends AbstractEntity<?>> void verifyEntities(final List<E> expected,
                                                              final String hqlQuery,
                                                              final Class<E> entityType,
                                                              final BiConsumer<E, E> equalChecker) {
        final List<E> actual = findEntities(hqlQuery, entityType);
        checkEquals(expected, actual, equalChecker);
    }

    private <E extends AbstractEntity<?>> List<E> findEntities(final String hqlQuery, final Class<E> entityType) {
        return entityManager.createQuery(hqlQuery, entityType).getResultList();
    }

    private boolean isAddressDeletedWithReplication(final Long id) {
        return !addressService.isExist(id) && !replicatedAddressRepository.existsById(id);
    }

    private boolean isAddressExistWithReplication(final Long id) {
        return addressService.isExist(id) && replicatedAddressRepository.existsById(id);
    }

    private void verifyNoReplicatedRepositoryMethodCall() {
        verifyNoInteractions(replicatedAddressRepository);
        verifyNoInteractions(replicatedPersonRepository);
    }

    private void verifyAttemptDeleteReplicatedAddress(final Long id) {
        verify(replicatedAddressRepository, times(1)).deleteById(eq(id));
    }

    private void verifyMaxAttemptDeleteReplicatedAddress(final Long id) {
        verify(replicatedAddressRepository, times(retryConsumeProperty.getMaxAttempts())).deleteById(eq(id));
    }

    private void verifyMaxAttemptSaveReplicatedPerson() {
        verify(replicatedPersonRepository, times(retryConsumeProperty.getMaxAttempts()))
                .save(any(ReplicatedPersonEntity.class));
    }

    private void verifyAttemptSaveReplicatedAddress() {
        verify(replicatedAddressRepository, times(1)).save(any(ReplicatedAddressEntity.class));
    }

    private void verifyReplicationAbsence(final Address address) {
        verifyReplicationAbsence(address, replicatedAddressRepository);
    }

    private void verifyReplicationAbsence(final Person person) {
        verifyReplicationAbsence(person, replicatedPersonRepository);
    }

    private <ID> void verifyReplicationAbsence(final AbstractDto<ID> dto,
                                               final JpaRepository<?, ID> replicationRepository) {
        assertFalse(replicationRepository.existsById(dto.getId()));
    }

    private Optional<Void> deleteAddress(final Long id) {
        addressService.delete(id);
        return empty();
    }

    private void deleteAddressInRollBackedTransaction(final Long id) {
        transactionTemplate.execute(
                status -> {
                    status.setRollbackOnly();
                    return deleteAddress(id);
                }
        );
    }

    private static AddressEntity createAddressEntity(final Long id) {
        return AddressEntity.builder()
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

    private static Address createAddress(final Long id) {
        return Address.builder()
                .id(id)
                .build();
    }

    private static Address createAddress(final String country, final String city) {
        return Address.builder()
                .country(country)
                .city(city)
                .build();
    }

    private static Person createPerson(final String name,
                                       final String surname,
                                       final String patronymic,
                                       final LocalDate birthDate,
                                       final Long addressId) {
        return createPerson(null, name, surname, patronymic, birthDate, addressId);
    }

    private static Person createPerson(final Long id,
                                       final String name,
                                       final String surname,
                                       final String patronymic,
                                       final LocalDate birthDate,
                                       final Long addressId) {
        return new Person(id, name, surname, patronymic, birthDate, createAddress(addressId));
    }

    private static ReplicatedAddressEntity createReplicatedAddress(final Long id) {
        return ReplicatedAddressEntity.builder()
                .id(id)
                .build();
    }

    private static ReplicatedPersonEntity createReplicatedPerson(final Long id,
                                                                 final String name,
                                                                 final String surname,
                                                                 final LocalDate birthDate,
                                                                 final Long addressId) {
        return new ReplicatedPersonEntity(id, name, surname, birthDate, createReplicatedAddress(addressId));
    }

    @Aspect
    @Component
    public static class ReplicationBarrier {
        private static final long TIMEOUT_DELTA_MS = 20000;
        private static final CountDownLatch DEFAULT_LATCH = new CountDownLatch(0);

        private final long timeoutMs;
        private volatile CountDownLatch addressLatch;
        private volatile CountDownLatch personLatch;
        private volatile boolean failedCallsCounted;

        public ReplicationBarrier(final ReplicationRetryConsumeProperty retryProperty) {
            timeoutMs = findTimeoutMs(retryProperty);
            addressLatch = DEFAULT_LATCH;
            personLatch = DEFAULT_LATCH;
        }

        public final void expect(final int addressCalls, final int personCalls, final boolean failedCallsCounted) {
            addressLatch = new CountDownLatch(addressCalls);
            personLatch = new CountDownLatch(personCalls);
            this.failedCallsCounted = failedCallsCounted;
        }

        @Around("replicatedAddressRepository()")
        public Object executeAddressMethod(final ProceedingJoinPoint joinPoint)
                throws Throwable {
            return executeCountingDownLatch(joinPoint, addressLatch);
        }

        @Around("replicatedPersonRepository()")
        public Object executePersonMethod(final ProceedingJoinPoint joinPoint)
                throws Throwable {
            return executeCountingDownLatch(joinPoint, personLatch);
        }

        public final void await() {
            await(addressLatch);
            await(personLatch);
        }

        private static long findTimeoutMs(final ReplicationRetryConsumeProperty retryProperty) {
            return retryProperty.getTimeLapseMs() * retryProperty.getMaxAttempts() + TIMEOUT_DELTA_MS;
        }

        private void await(final CountDownLatch latch) {
            try {
                awaitInterrupted(latch);
            } catch (final InterruptedException cause) {
                throw new IllegalStateException(cause);
            }
        }

        private void awaitInterrupted(final CountDownLatch latch)
                throws InterruptedException {
            final boolean timeoutExceeded = !latch.await(timeoutMs, MILLISECONDS);
            if (timeoutExceeded) {
                throw new IllegalStateException("Latch timeout was exceeded");
            }
        }

        private Object executeCountingDownLatch(final ProceedingJoinPoint joinPoint, final CountDownLatch latch)
                throws Throwable {
            try {
                final Object result = joinPoint.proceed();
                latch.countDown();
                return result;
            } catch (final Throwable exception) {
                onFailedExecution(latch);
                throw exception;
            }
        }

        private void onFailedExecution(final CountDownLatch latch) {
            if (failedCallsCounted) {
                latch.countDown();
            }
        }

        @Pointcut("target(by.aurorasoft.testapp.crud.v2.repository.ReplicatedAddressRepository)")
        private void replicatedAddressRepository() {

        }

        @Pointcut("target(by.aurorasoft.testapp.crud.v2.repository.ReplicatedPersonRepository)")
        private void replicatedPersonRepository() {

        }
    }
}
