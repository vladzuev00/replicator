package by.aurorasoft.testapp.it;

import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
import by.aurorasoft.testapp.base.AbstractSpringBootTest;
import by.aurorasoft.testapp.crud.dto.Address;
import by.aurorasoft.testapp.crud.dto.Person;
import by.aurorasoft.testapp.crud.entity.*;
import by.aurorasoft.testapp.crud.repository.ReplicatedAddressRepository;
import by.aurorasoft.testapp.crud.repository.ReplicatedPersonRepository;
import by.aurorasoft.testapp.model.AddressName;
import by.aurorasoft.testapp.model.PersonAddress;
import by.aurorasoft.testapp.model.PersonName;
import by.aurorasoft.testapp.testutil.AddressEntityUtil;
import by.aurorasoft.testapp.testutil.PersonEntityUtil;
import by.aurorasoft.testapp.testutil.ReplicatedAddressEntityUtil;
import by.aurorasoft.testapp.testutil.ReplicatedPersonEntityUtil;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.lang.Long.MAX_VALUE;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Import(ReplicationIT.ReplicationRepositoryBarrier.class)
public abstract class ReplicationIT<ADDRESS extends Address, PERSON extends Person>
        extends AbstractSpringBootTest {
    private static final String FOREIGN_KEY_VIOLATION_SQL_STATE = "23503";
    private static final String UNIQUE_VIOLATION_SQL_STATE = "23505";

    @Autowired
    private ReplicationRetryConsumeProperty retryConsumeProperty;

    @Autowired
    private ReplicationRepositoryBarrier replicationRepositoryBarrier;

    @SpyBean
    private ReplicatedAddressRepository replicatedAddressRepository;

    @SpyBean
    private ReplicatedPersonRepository replicatedPersonRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void addressShouldBeSaved() {
        final String givenCountry = "Belarus";
        final String givenCity = "Minsk";
        final ADDRESS givenAddress = createAddress(givenCountry, givenCity);

        final ADDRESS actual = executeWaitingReplication(() -> save(givenAddress), 1, 0, false);
        final ADDRESS expected = createAddress(1L, givenCountry, givenCity);
        assertEquals(expected, actual);

        verifyReplicationFor(actual);
    }

    @Test
    public void addressShouldNotBeSavedBecauseOfUniqueViolation() {
        final ADDRESS givenAddress = createAddress("Russia", "Moscow");

        executeExpectingUniqueViolation(() -> save(givenAddress));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void personShouldNotBeSavedBecauseOfForeignKeyViolation() {
        final PERSON givenPerson = createPerson("Harry", "Potter", "Sergeevich", LocalDate.of(1990, 8, 4), 254L);

        executeExpectingForeignKeyViolation(() -> save(givenPerson));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressesShouldBeSaved() {
        final String firstGivenCountry = "China";
        final String secondGivenCountry = "China";
        final String firstGivenCity = "Fuyang";
        final String secondGivenCity = "Hefei";
        final List<ADDRESS> givenAddresses = List.of(
                createAddress(firstGivenCountry, firstGivenCity),
                createAddress(secondGivenCountry, secondGivenCity)
        );

        final List<ADDRESS> actual = executeWaitingReplication(
                () -> saveAddresses(givenAddresses),
                givenAddresses.size(),
                0,
                false
        );
        final List<ADDRESS> expected = List.of(
                createAddress(1L, firstGivenCountry, firstGivenCity),
                createAddress(2L, secondGivenCountry, secondGivenCity)
        );
        assertEquals(expected, actual);

        verifyReplicationsFor(actual);
    }

    @Test
    public void addressesShouldNotBeSavedBecauseOfUniqueViolation() {
        final List<ADDRESS> givenAddresses = List.of(
                createAddress("Belarus", "Minsk"),
                createAddress("Russia", "Moscow")
        );

        executeExpectingUniqueViolation(() -> saveAddresses(givenAddresses));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void personsShouldNotBeSavedBecauseOfForeignKeyViolation() {
        final List<PERSON> givenPersons = List.of(
                createPerson("Avdifaks", "Kuznetsov", "Vasilievich", LocalDate.of(1995, 7, 2), 255L),
                createPerson("Harry", "Potter", "Sergeevich", LocalDate.of(1990, 8, 4), 254L)
        );

        executeExpectingForeignKeyViolation(() -> savePersons(givenPersons));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeUpdated() {
        final ADDRESS givenAddress = createAddress(255L, "Belarus", "Minsk");

        final ADDRESS actual = executeWaitingReplication(() -> update(givenAddress), 1, 0, false);
        assertEquals(givenAddress, actual);

        verifyReplicationFor(actual);
    }

    @Test
    public void addressShouldNotBeUpdatedBecauseOfUniqueViolation() {
        final ADDRESS givenAddress = createAddress(256L, "Russia", "Moscow");

        executeExpectingUniqueViolation(() -> update(givenAddress));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void personShouldNotBeUpdatedBecauseOfNoSuchAddress() {
        final PERSON givenPerson = createPerson(255L, "Vlad", "Zuev", "Sergeevich", LocalDate.of(2000, 2, 18), 254L);

        executeExpectingNoSuchEntityException(() -> update(givenPerson));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeUpdatedPartially() {
        final Long givenId = 255L;
        final AddressName givenNewName = new AddressName("Belarus", "Minsk");

        final ADDRESS actual = executeWaitingReplication(
                () -> updateAddressPartial(givenId, givenNewName),
                1,
                0,
                false
        );
        final ADDRESS expected = createAddress(givenId, givenNewName.getCountry(), givenNewName.getCity());
        assertEquals(expected, actual);

        verifyReplicationFor(actual);
    }

    @Test
    public void addressShouldNotBeUpdatedPartiallyBecauseOfUniqueViolation() {
        final Long givenId = 256L;
        final AddressName givenNewName = new AddressName("Russia", "Moscow");

        executeExpectingUniqueViolation(() -> updateAddressPartial(givenId, givenNewName));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void personShouldNotBeUpdatedPartiallyBecauseOfNoSuchAddress() {
        final Long givenId = 255L;
        final PersonAddress givenNewAddress = new PersonAddress(createAddress(254L));

        executeExpectingNoSuchEntityException(() -> updatePersonPartial(givenId, givenNewAddress));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeDeleted() {
        final Long givenId = 262L;

        executeWaitingReplication(() -> deleteAddress(givenId), 1, 0, false);

        assertFalse(isAddressExist(givenId));
        assertFalse(replicatedAddressRepository.existsById(givenId));
    }

    @Test
    public void addressShouldNotBeDeletedByNotExistId() {
        final Long givenId = MAX_VALUE;

        executeWaitingReplication(() -> deleteAddress(givenId), 1, 0, true);

        verify(replicatedAddressRepository, times(1)).deleteById(eq(givenId));
    }

    @Test
    public void addressShouldNotBeDeletedBecauseOfForeignKeyViolation() {
        final Long givenId = 255L;

        executeExpectingForeignKeyViolation(() -> deleteAddress(givenId));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void operationsShouldBeExecuted() {
        executeWaitingReplication(
                () -> {
                    saveAddresses(
                            List.of(
                                    createAddress("China", "Hong Kong"),
                                    createAddress("China", "Anqing"),
                                    createAddress("China", "Bozhou")
                            )
                    );
                    savePersons(
                            List.of(
                                    createPerson("Avdifaks", "Kuznetsov", "Vasilievich", LocalDate.of(1995, 7, 2), 1L),
                                    createPerson("Vitenka", "Kozar", "Vadimovich", LocalDate.of(1996, 6, 1), 2L),
                                    createPerson("Yury", "Sitnikov", "Stepanovich", LocalDate.of(1997, 8, 3), 3L)
                            )
                    );
                    save(createAddress("China", "Huainan"));
                    executeExpectingUniqueViolation(() -> save(createAddress("China", "Huainan")));
                    executeExpectingUniqueViolation(() -> save(createAddress("Russia", "Moscow")));
                    updateAddressPartial(4L, new AddressName("Belarus", "Gomel"));
                    updatePersonPartial(2L, new PersonName("Ivan", "Zuev", "Ivanovich"));
                    deletePerson(MAX_VALUE);
                    deleteAddress(MAX_VALUE);
                    executeExpectingUniqueViolation(() -> updateAddressPartial(256L, new AddressName("Russia", "Moscow")));
                    deletePerson(259L);
                    deleteAddress(257L);
                    update(createPerson(257L, "Alexandr", "Verbitskiy", "Dmitrievich", LocalDate.of(2000, 5, 20), 256L));
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

        assertFalse(isAddressExist(givenId));
        assertTrue(replicatedAddressRepository.existsById(givenId));

        verify(replicatedAddressRepository, times(retryConsumeProperty.getMaxAttempts())).deleteById(eq(givenId));
    }

    @Test
    public void personShouldBeSavedButNotReplicatedBecauseOfForeignKeyViolation() {
        final PERSON givenPerson = createPerson("Petr", "Ivanov", "Petrovich", LocalDate.of(2000, 3, 19), 264L);

        final PERSON actual = executeWaitingReplication(() -> save(givenPerson), 0, retryConsumeProperty.getMaxAttempts(), true);
        final PERSON expected = createPerson(
                1L,
                givenPerson.getName(),
                givenPerson.getSurname(),
                givenPerson.getPatronymic(),
                givenPerson.getBirthDate(),
                createAddress(264L)
        );
        assertEquals(expected, actual);

        assertFalse(replicatedPersonRepository.existsById(actual.getId()));

        verify(
                replicatedPersonRepository,
                times(retryConsumeProperty.getMaxAttempts())
        ).save(any(ReplicatedPersonEntity.class));
    }

    @Test
    public void addressShouldBeSavedButNotReplicatedBecauseOfUniqueConstraint() {
        final ADDRESS givenAddress = createAddress("Japan", "Tokyo");

        final ADDRESS actual = executeWaitingReplication(() -> save(givenAddress), 1, 0, true);
        final ADDRESS expected = createAddress(1L, givenAddress.getCountry(), givenAddress.getCity());
        assertEquals(expected, actual);

        assertFalse(replicatedAddressRepository.existsById(actual.getId()));
        verify(replicatedAddressRepository, times(1)).save(any(ReplicatedAddressEntity.class));
    }

    @Test
    public void addressShouldNotBeDeletedBecauseOfTransactionRollBacked() {
        final Long givenId = 262L;

        deleteAddressInRollBackedTransaction(givenId);

        assertTrue(isAddressExist(givenId));
        assertTrue(replicatedAddressRepository.existsById(givenId));
    }

    protected abstract ADDRESS createAddress(final Long id, final String country, final String city);

    protected abstract PERSON createPerson(final Long id,
                                           final String name,
                                           final String surname,
                                           final String patronymic,
                                           final LocalDate birthDate,
                                           final ADDRESS address);

    protected abstract ADDRESS save(final ADDRESS address);

    protected abstract PERSON save(final PERSON person);

    protected abstract List<ADDRESS> saveAddresses(final List<ADDRESS> addresses);

    @SuppressWarnings("UnusedReturnValue")
    protected abstract List<PERSON> savePersons(final List<PERSON> persons);

    protected abstract ADDRESS update(final ADDRESS address);

    @SuppressWarnings("UnusedReturnValue")
    protected abstract PERSON update(final PERSON person);

    protected abstract ADDRESS updateAddressPartial(final Long id, final Object partial);

    @SuppressWarnings("UnusedReturnValue")
    protected abstract PERSON updatePersonPartial(final Long id, final Object partial);

    protected abstract void deleteAddress(final Long id);

    protected abstract void deletePerson(final Long id);

    protected abstract boolean isAddressExist(final Long id);

    private ADDRESS createAddress(final Long id) {
        return createAddress(id, null, null);
    }

    private ADDRESS createAddress(final String country, final String city) {
        return createAddress(null, country, city);
    }

    private PERSON createPerson(final String name,
                                final String surname,
                                final String patronymic,
                                final LocalDate birthDate,
                                final Long addressId) {
        return createPerson(null, name, surname, patronymic, birthDate, addressId);
    }

    private PERSON createPerson(final Long id,
                                final String name,
                                final String surname,
                                final String patronymic,
                                final LocalDate birthDate,
                                final Long addressId) {
        final ADDRESS address = createAddress(addressId);
        return createPerson(id, name, surname, patronymic, birthDate, address);
    }

    private void executeWaitingReplication(final Runnable operation,
                                           final int addressCalls,
                                           @SuppressWarnings("SameParameterValue") final int personCalls,
                                           final boolean failedCallsCounted) {
        executeWaitingReplication(
                () -> {
                    operation.run();
                    return empty();
                },
                addressCalls,
                personCalls,
                failedCallsCounted
        );
    }

    private <R> R executeWaitingReplication(final Supplier<R> operation,
                                            final int addressCalls,
                                            final int personCalls,
                                            final boolean failedCallsCounted) {
        replicationRepositoryBarrier.expect(addressCalls, personCalls, failedCallsCounted);
        final R result = operation.get();
        replicationRepositoryBarrier.await();
        return result;
    }

    private void verifyReplicationFor(final ADDRESS address) {
        verifyReplicationsFor(singletonList(address));
    }

    private void verifyReplicationsFor(final List<ADDRESS> addresses) {
        final List<ReplicatedAddressEntity> expected = mapToReplicatedAddresses(addresses);
        verifyReplicatedAddresses(expected);
    }

    private List<ReplicatedAddressEntity> mapToReplicatedAddresses(final List<ADDRESS> addresses) {
        return addresses.stream()
                .map(this::mapToReplicatedAddress)
                .toList();
    }

    private ReplicatedAddressEntity mapToReplicatedAddress(final Address address) {
        return new ReplicatedAddressEntity(address.getId(), address.getCountry(), address.getCity());
    }

    private void executeExpectingUniqueViolation(final Runnable task) {
        executeExpectingSqlConstraintViolation(task, UNIQUE_VIOLATION_SQL_STATE);
    }

    private void executeExpectingForeignKeyViolation(final Runnable task) {
        executeExpectingSqlConstraintViolation(task, FOREIGN_KEY_VIOLATION_SQL_STATE);
    }

    private void executeExpectingSqlConstraintViolation(final Runnable task, final String sqlState) {
        executeExpectingMatchingException(task, exception -> Objects.equals(sqlState, getSqlState(exception)));
    }

    private void executeExpectingNoSuchEntityException(final Runnable task) {
        executeExpectingMatchingException(task, exception -> getRootCause(exception).getClass() == EntityNotFoundException.class);
    }

    private void executeExpectingMatchingException(final Runnable task, final Predicate<Throwable> predicate) {
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

    private String getSqlState(final Throwable exception) {
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

    private <E extends Entity> void verifyEntities(final List<E> expected,
                                                   final String hqlQuery,
                                                   final Class<E> entityType,
                                                   final BiConsumer<E, E> equalChecker) {
        final List<E> actual = entityManager.createQuery(hqlQuery, entityType).getResultList();
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> equalChecker.accept(expected.get(i), actual.get(i)));
    }

    private void verifyNoReplicationRepositoryMethodCall() {
        verifyNoInteractions(replicatedAddressRepository, replicatedPersonRepository);
    }

    private void deleteAddressInRollBackedTransaction(final Long id) {
        transactionTemplate.execute(
                status -> {
                    status.setRollbackOnly();
                    deleteAddress(id);
                    return empty();
                }
        );
    }

    private PersonEntity createPersonEntity(final Long id,
                                            final String name,
                                            final String surname,
                                            final String patronymic,
                                            final LocalDate birthDate,
                                            final Long addressId) {
        return new PersonEntity(id, name, surname, patronymic, birthDate, createAddressEntity(addressId));
    }

    private AddressEntity createAddressEntity(final Long id) {
        return AddressEntity.builder()
                .id(id)
                .build();
    }

    private ReplicatedAddressEntity createReplicatedAddress(final Long id) {
        return ReplicatedAddressEntity.builder()
                .id(id)
                .build();
    }

    private ReplicatedPersonEntity createReplicatedPerson(final Long id,
                                                          final String name,
                                                          final String surname,
                                                          final LocalDate birthDate,
                                                          final Long addressId) {
        return new ReplicatedPersonEntity(id, name, surname, birthDate, createReplicatedAddress(addressId));
    }

    @Aspect
    @Component
    public static class ReplicationRepositoryBarrier {
        private static final long TIMEOUT_DELTA_MS = 20000;
        private static final CountDownLatch DEFAULT_LATCH = new CountDownLatch(0);

        private final long timeoutMs;
        private volatile CountDownLatch addressLatch;
        private volatile CountDownLatch personLatch;
        private volatile boolean failedCallsCounted;

        public ReplicationRepositoryBarrier(final ReplicationRetryConsumeProperty retryProperty) {
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

        private long findTimeoutMs(final ReplicationRetryConsumeProperty retryProperty) {
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

        @Pointcut("target(by.aurorasoft.testapp.crud.repository.ReplicatedAddressRepository)")
        private void replicatedAddressRepository() {

        }

        @Pointcut("target(by.aurorasoft.testapp.crud.repository.ReplicatedPersonRepository)")
        private void replicatedPersonRepository() {

        }
    }
}
