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

import static by.aurorasoft.testapp.testutil.EntityUtil.checkEquals;
import static java.lang.Long.MAX_VALUE;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Import(ReplicationIT.ReplicationRepositoryBarrier.class)
public abstract class ReplicationIT<ADDRESS extends Address, PERSON extends Person>
        extends AbstractSpringBootTest {
    private static final String FOREIGN_KEY_VIOLATION_SQL_STATE = "23503";
    private static final String UNIQUE_VIOLATION_SQL_STATE = "23505";

    @SpyBean
    protected ReplicatedAddressRepository replicatedAddressRepository;

    @SpyBean
    protected ReplicatedPersonRepository replicatedPersonRepository;

    @Autowired
    private ReplicationRetryConsumeProperty retryConsumeProperty;

    @Autowired
    private ReplicationRepositoryBarrier replicationRepositoryBarrier;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void addressShouldBeSaved() {
        String givenCountry = "Belarus";
        String givenCity = "Minsk";
        ADDRESS givenAddress = createAddress(givenCountry, givenCity);

        ADDRESS actual = executeWaitingReplication(() -> save(givenAddress), 1, 0, false);
        ADDRESS expected = createAddress(1L, givenCountry, givenCity);
        assertEquals(expected, actual);

        verifyReplicationFor(actual);
    }

    @Test
    public void addressShouldNotBeSavedBecauseOfUniqueViolation() {
        ADDRESS givenAddress = createAddress("Russia", "Moscow");

        executeExpectingUniqueViolation(() -> save(givenAddress));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void personShouldNotBeSavedBecauseOfForeignKeyViolation() {
        PERSON givenPerson = createPerson("Harry", "Potter", "Sergeevich", LocalDate.of(1990, 8, 4), 254L);

        executeExpectingForeignKeyViolation(() -> save(givenPerson));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressesShouldBeSaved() {
        String firstGivenCountry = "China";
        String secondGivenCountry = "China";
        String firstGivenCity = "Fuyang";
        String secondGivenCity = "Hefei";
        List<ADDRESS> givenAddresses = List.of(
                createAddress(firstGivenCountry, firstGivenCity),
                createAddress(secondGivenCountry, secondGivenCity)
        );

        List<ADDRESS> actual = executeWaitingReplication(
                () -> saveAddresses(givenAddresses),
                givenAddresses.size(),
                0,
                false
        );
        List<ADDRESS> expected = List.of(
                createAddress(1L, firstGivenCountry, firstGivenCity),
                createAddress(2L, secondGivenCountry, secondGivenCity)
        );
        assertEquals(expected, actual);

        verifyReplicationsFor(actual);
    }

    @Test
    public void addressesShouldNotBeSavedBecauseOfUniqueViolation() {
        List<ADDRESS> givenAddresses = List.of(
                createAddress("Belarus", "Minsk"),
                createAddress("Russia", "Moscow")
        );

        executeExpectingUniqueViolation(() -> saveAddresses(givenAddresses));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void personsShouldNotBeSavedBecauseOfForeignKeyViolation() {
        List<PERSON> givenPersons = List.of(
                createPerson("Avdifaks", "Kuznetsov", "Vasilievich", LocalDate.of(1995, 7, 2), 255L),
                createPerson("Harry", "Potter", "Sergeevich", LocalDate.of(1990, 8, 4), 254L)
        );

        executeExpectingForeignKeyViolation(() -> savePersons(givenPersons));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeUpdated() {
        ADDRESS givenAddress = createAddress(255L, "Belarus", "Minsk");

        ADDRESS actual = executeWaitingReplication(() -> update(givenAddress), 1, 0, false);
        assertEquals(givenAddress, actual);

        verifyReplicationFor(actual);
    }

    @Test
    public void addressShouldNotBeUpdatedBecauseOfUniqueViolation() {
        ADDRESS givenAddress = createAddress(256L, "Russia", "Moscow");

        executeExpectingUniqueViolation(() -> update(givenAddress));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void personShouldNotBeUpdatedBecauseOfNoSuchAddress() {
        PERSON givenPerson = createPerson(255L, "Vlad", "Zuev", "Sergeevich", LocalDate.of(2000, 2, 18), 254L);

        executeExpectingNoSuchEntityException(() -> update(givenPerson));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeUpdatedPartially() {
        Long givenId = 255L;
        AddressName givenNewName = new AddressName("Belarus", "Minsk");

        ADDRESS actual = executeWaitingReplication(
                () -> updateAddressPartial(givenId, givenNewName),
                1,
                0,
                false
        );
        ADDRESS expected = createAddress(givenId, givenNewName.getCountry(), givenNewName.getCity());
        assertEquals(expected, actual);

        verifyReplicationFor(actual);
    }

    @Test
    public void addressShouldNotBeUpdatedPartiallyBecauseOfUniqueViolation() {
        Long givenId = 256L;
        AddressName givenNewName = new AddressName("Russia", "Moscow");

        executeExpectingUniqueViolation(() -> updateAddressPartial(givenId, givenNewName));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void personShouldNotBeUpdatedPartiallyBecauseOfNoSuchAddress() {
        Long givenId = 255L;
        PersonAddress givenNewAddress = new PersonAddress(createAddress(254L));

        executeExpectingNoSuchEntityException(() -> updatePersonPartial(givenId, givenNewAddress));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeDeleted() {
        Long givenId = 262L;

        executeWaitingReplication(() -> deleteAddress(givenId), 1, 0, false);

        assertFalse(isAddressExist(givenId));
        assertFalse(replicatedAddressRepository.existsById(givenId));
    }

    @Test
    public void addressShouldNotBeDeletedByNotExistId() {
        Long givenId = MAX_VALUE;

        executeWaitingReplication(() -> deleteAddress(givenId), 1, 0, true);

        verify(replicatedAddressRepository, times(1)).deleteById(eq(givenId));
    }

    @Test
    public void addressShouldNotBeDeletedBecauseOfForeignKeyViolation() {
        Long givenId = 255L;

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
        Long givenId = 258L;

        executeWaitingReplication(() -> deleteAddress(givenId), retryConsumeProperty.getMaxAttempts(), 0, true);

        assertFalse(isAddressExist(givenId));
        assertTrue(replicatedAddressRepository.existsById(givenId));

        verify(replicatedAddressRepository, times(retryConsumeProperty.getMaxAttempts())).deleteById(eq(givenId));
    }

    @Test
    public void personShouldBeSavedButNotReplicatedBecauseOfForeignKeyViolation() {
        PERSON givenPerson = createPerson("Petr", "Ivanov", "Petrovich", LocalDate.of(2000, 3, 19), 264L);

        PERSON actual = executeWaitingReplication(() -> save(givenPerson), 0, retryConsumeProperty.getMaxAttempts(), true);
        PERSON expected = createPerson(
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
        ADDRESS givenAddress = createAddress("Japan", "Tokyo");

        ADDRESS actual = executeWaitingReplication(() -> save(givenAddress), 1, 0, true);
        ADDRESS expected = createAddress(1L, givenAddress.getCountry(), givenAddress.getCity());
        assertEquals(expected, actual);

        assertFalse(replicatedAddressRepository.existsById(actual.getId()));
        verify(replicatedAddressRepository, times(1)).save(any(ReplicatedAddressEntity.class));
    }

    @Test
    public void addressShouldNotBeDeletedBecauseOfTransactionRollBacked() {
        Long givenId = 262L;

        deleteAddressInRollBackedTransaction(givenId);

        assertTrue(isAddressExist(givenId));
        assertTrue(replicatedAddressRepository.existsById(givenId));
    }

    protected abstract ADDRESS createAddress(Long id, String country, String city);

    protected abstract PERSON createPerson(Long id,
                                           String name,
                                           String surname,
                                           String patronymic,
                                           LocalDate birthDate,
                                           ADDRESS address);

    protected abstract ADDRESS save(ADDRESS address);

    protected abstract PERSON save(PERSON person);

    protected abstract List<ADDRESS> saveAddresses(List<ADDRESS> addresses);

    @SuppressWarnings("UnusedReturnValue")
    protected abstract List<PERSON> savePersons(List<PERSON> persons);

    protected abstract ADDRESS update(ADDRESS address);

    @SuppressWarnings("UnusedReturnValue")
    protected abstract PERSON update(PERSON person);

    protected abstract ADDRESS updateAddressPartial(Long id, Object partial);

    @SuppressWarnings("UnusedReturnValue")
    protected abstract PERSON updatePersonPartial(Long id, Object partial);

    protected abstract void deleteAddress(Long id);

    protected abstract void deletePerson(Long id);

    protected abstract boolean isAddressExist(Long id);

    protected final ADDRESS createAddress(Long id) {
        return createAddress(id, null, null);
    }

    protected void executeWaitingReplication(Runnable operation,
                                             int addressCalls,
                                             @SuppressWarnings("SameParameterValue") int personCalls,
                                             boolean failedCallsCounted) {
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

    private ADDRESS createAddress(String country, String city) {
        return createAddress(null, country, city);
    }

    private PERSON createPerson(String name,
                                String surname,
                                String patronymic,
                                LocalDate birthDate,
                                Long addressId) {
        return createPerson(null, name, surname, patronymic, birthDate, addressId);
    }

    private PERSON createPerson(Long id,
                                String name,
                                String surname,
                                String patronymic,
                                LocalDate birthDate,
                                Long addressId) {
        ADDRESS address = createAddress(addressId);
        return createPerson(id, name, surname, patronymic, birthDate, address);
    }

    private <R> R executeWaitingReplication(Supplier<R> operation,
                                            int addressCalls,
                                            int personCalls,
                                            boolean failedCallsCounted) {
        replicationRepositoryBarrier.expect(addressCalls, personCalls, failedCallsCounted);
        R result = operation.get();
        replicationRepositoryBarrier.await();
        return result;
    }

    private void verifyReplicationFor(ADDRESS address) {
        verifyReplicationsFor(singletonList(address));
    }

    private void verifyReplicationsFor(List<ADDRESS> addresses) {
        List<Long> ids = mapToIds(addresses);
        List<ReplicatedAddressEntity> actual = findReplicatedAddressesOrderedById(ids);
        List<ReplicatedAddressEntity> expected = mapToReplicatedAddresses(addresses);
        ReplicatedAddressEntityUtil.checkEquals(expected, actual);
    }

    private List<Long> mapToIds(List<ADDRESS> addresses) {
        return addresses.stream()
                .map(Address::getId)
                .toList();
    }

    private List<ReplicatedAddressEntity> findReplicatedAddressesOrderedById(List<Long> ids) {
        return entityManager.createQuery("SELECT e FROM ReplicatedAddressEntity e WHERE e.id IN :ids ORDER BY e.id", ReplicatedAddressEntity.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    private List<ReplicatedAddressEntity> mapToReplicatedAddresses(List<ADDRESS> addresses) {
        return addresses.stream()
                .map(this::mapToReplicatedAddress)
                .toList();
    }

    private ReplicatedAddressEntity mapToReplicatedAddress(Address address) {
        return new ReplicatedAddressEntity(address.getId(), address.getCountry(), address.getCity());
    }

    private void executeExpectingUniqueViolation(Runnable task) {
        executeExpectingSqlConstraintViolation(task, UNIQUE_VIOLATION_SQL_STATE);
    }

    private void executeExpectingForeignKeyViolation(Runnable task) {
        executeExpectingSqlConstraintViolation(task, FOREIGN_KEY_VIOLATION_SQL_STATE);
    }

    private void executeExpectingSqlConstraintViolation(Runnable task, String sqlState) {
        executeExpectingMatchingException(task, exception -> Objects.equals(sqlState, getSqlState(exception)));
    }

    private void executeExpectingNoSuchEntityException(Runnable task) {
        executeExpectingMatchingException(task, exception -> getRootCause(exception).getClass() == EntityNotFoundException.class);
    }

    private void executeExpectingMatchingException(Runnable task, Predicate<Throwable> predicate) {
        boolean exceptionArisen;
        try {
            task.run();
            exceptionArisen = false;
        } catch (Throwable exception) {
            exceptionArisen = true;
            assertTrue(predicate.test(exception));
        }
        assertTrue(exceptionArisen);
    }

    private String getSqlState(Throwable exception) {
        return ((SQLException) getRootCause(exception)).getSQLState();
    }

    private void verifyDatabase(List<AddressEntity> expectedAddresses,
                                List<PersonEntity> expectedPersons,
                                List<ReplicatedAddressEntity> expectedReplicatedAddresses,
                                List<ReplicatedPersonEntity> expectedReplicatedPersons) {
        verifyAddresses(expectedAddresses);
        verifyPersons(expectedPersons);
        verifyReplicatedAddresses(expectedReplicatedAddresses);
        verifyReplicatedPersons(expectedReplicatedPersons);
    }

    private void verifyAddresses(List<AddressEntity> expected) {
        verifyEntities(
                expected,
                "SELECT e FROM AddressEntity e ORDER BY e.id",
                AddressEntity.class,
                AddressEntityUtil::checkEquals
        );
    }

    private void verifyPersons(List<PersonEntity> expected) {
        verifyEntities(
                expected,
                "SELECT e FROM PersonEntity e ORDER BY e.id",
                PersonEntity.class,
                PersonEntityUtil::checkEquals
        );
    }

    private void verifyReplicatedAddresses(List<ReplicatedAddressEntity> expected) {
        verifyEntities(
                expected,
                "SELECT e FROM ReplicatedAddressEntity e ORDER BY e.id",
                ReplicatedAddressEntity.class,
                ReplicatedAddressEntityUtil::checkEquals
        );
    }

    private void verifyReplicatedPersons(List<ReplicatedPersonEntity> expected) {
        verifyEntities(
                expected,
                "SELECT e FROM ReplicatedPersonEntity e ORDER BY e.id",
                ReplicatedPersonEntity.class,
                ReplicatedPersonEntityUtil::checkEquals
        );
    }

    private <E extends Entity> void verifyEntities(List<E> expected,
                                                   String hqlQuery,
                                                   Class<E> entityType,
                                                   BiConsumer<E, E> equalChecker) {
        List<E> actual = entityManager.createQuery(hqlQuery, entityType).getResultList();
        checkEquals(expected, actual, equalChecker);
    }

    private void verifyNoReplicationRepositoryMethodCall() {
        verifyNoInteractions(replicatedAddressRepository, replicatedPersonRepository);
    }

    private void deleteAddressInRollBackedTransaction(Long id) {
        transactionTemplate.execute(
                status -> {
                    status.setRollbackOnly();
                    deleteAddress(id);
                    return empty();
                }
        );
    }

    private PersonEntity createPersonEntity(Long id,
                                            String name,
                                            String surname,
                                            String patronymic,
                                            LocalDate birthDate,
                                            Long addressId) {
        return new PersonEntity(id, name, surname, patronymic, birthDate, createAddressEntity(addressId));
    }

    private AddressEntity createAddressEntity(Long id) {
        return AddressEntity.builder()
                .id(id)
                .build();
    }

    private ReplicatedAddressEntity createReplicatedAddress(Long id) {
        return ReplicatedAddressEntity.builder()
                .id(id)
                .build();
    }

    private ReplicatedPersonEntity createReplicatedPerson(Long id,
                                                          String name,
                                                          String surname,
                                                          LocalDate birthDate,
                                                          Long addressId) {
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

        public ReplicationRepositoryBarrier(ReplicationRetryConsumeProperty retryProperty) {
            timeoutMs = findTimeoutMs(retryProperty);
            addressLatch = DEFAULT_LATCH;
            personLatch = DEFAULT_LATCH;
        }

        public final void expect(int addressCalls, int personCalls, boolean failedCallsCounted) {
            addressLatch = new CountDownLatch(addressCalls);
            personLatch = new CountDownLatch(personCalls);
            this.failedCallsCounted = failedCallsCounted;
        }

        @Around("replicatedAddressRepository()")
        public Object executeAddressMethod(ProceedingJoinPoint joinPoint)
                throws Throwable {
            return executeCountingDownLatch(joinPoint, addressLatch);
        }

        @Around("replicatedPersonRepository()")
        public Object executePersonMethod(ProceedingJoinPoint joinPoint)
                throws Throwable {
            return executeCountingDownLatch(joinPoint, personLatch);
        }

        public final void await() {
            await(addressLatch);
            await(personLatch);
        }

        private long findTimeoutMs(ReplicationRetryConsumeProperty retryProperty) {
            return retryProperty.getTimeLapseMs() * retryProperty.getMaxAttempts() + TIMEOUT_DELTA_MS;
        }

        private void await(CountDownLatch latch) {
            try {
                awaitInterrupted(latch);
            } catch (InterruptedException cause) {
                throw new IllegalStateException(cause);
            }
        }

        private void awaitInterrupted(CountDownLatch latch)
                throws InterruptedException {
            boolean timeoutExceeded = !latch.await(timeoutMs, MILLISECONDS);
            if (timeoutExceeded) {
                throw new IllegalStateException("Latch timeout was exceeded");
            }
        }

        private Object executeCountingDownLatch(ProceedingJoinPoint joinPoint, CountDownLatch latch)
                throws Throwable {
            try {
                Object result = joinPoint.proceed();
                latch.countDown();
                return result;
            } catch (Throwable exception) {
                onFailedExecution(latch);
                throw exception;
            }
        }

        private void onFailedExecution(CountDownLatch latch) {
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
