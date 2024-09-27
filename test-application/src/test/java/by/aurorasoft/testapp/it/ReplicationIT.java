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
import by.aurorasoft.testapp.testutil.AddressEntityUtil;
import by.aurorasoft.testapp.testutil.PersonEntityUtil;
import by.aurorasoft.testapp.testutil.ReplicatedAddressEntityUtil;
import by.aurorasoft.testapp.testutil.ReplicatedPersonEntityUtil;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.lang.Long.MAX_VALUE;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.core.NestedExceptionUtils.getRootCause;

@Import(ReplicationIT.ReplicationDeliveryBarrier.class)
public final class ReplicationIT extends AbstractSpringBootTest {
    private static final String FOREIGN_KEY_VIOLATION_SQL_STATE = "23503";
    private static final String UNIQUE_VIOLATION_SQL_STATE = "23505";

    @Autowired
    private AddressService addressService;

    @Autowired
    private PersonService personService;

    @SpyBean
    private ReplicatedAddressRepository replicatedAddressRepository;

    @SpyBean
    private ReplicatedPersonRepository replicatedPersonRepository;

    @Autowired
    private ReplicationRetryConsumeProperty retryConsumeProperty;

    @Autowired
    private ReplicationDeliveryBarrier replicationDeliveryBarrier;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void addressShouldBeSaved() {
        String givenCountry = "Belarus";
        String givenCity = "Minsk";
        Address givenAddress = Address.builder()
                .country(givenCountry)
                .city(givenCity)
                .build();

        Address actual = executeWaitingReplication(() -> addressService.save(givenAddress), 1, 0, true);
        Address expected = new Address(1L, givenCountry, givenCity);
        assertEquals(expected, actual);

        verifyReplicationFor(actual);
    }

    @Test
    public void addressShouldNotBeSavedBecauseOfUniqueViolation() {
        Address givenAddress = Address.builder()
                .country("Russia")
                .city("Moscow")
                .build();

        executeExpectingUniqueViolation(() -> addressService.save(givenAddress));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void personShouldNotBeSavedBecauseOfForeignKeyViolation() {
        Person givenPerson = Person.builder()
                .name("Harry")
                .surname("Potter")
                .patronymic("Sergeevich")
                .birthDate(LocalDate.of(1990, 8, 4))
                .address(Address.builder().id(254L).build())
                .build();

        executeExpectingForeignKeyViolation(() -> personService.save(givenPerson));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressesShouldBeSaved() {
        String firstGivenCountry = "China";
        String secondGivenCountry = "China";
        String firstGivenCity = "Fuyang";
        String secondGivenCity = "Hefei";
        List<Address> givenAddresses = List.of(
                Address.builder().country(firstGivenCountry).city(firstGivenCity).build(),
                Address.builder().country(secondGivenCountry).city(secondGivenCity).build()
        );

        List<Address> actual = executeWaitingReplication(
                () -> addressService.saveAll(givenAddresses),
                givenAddresses.size(),
                0,
                true
        );
        List<Address> expected = List.of(
                new Address(1L, firstGivenCountry, firstGivenCity),
                new Address(2L, secondGivenCountry, secondGivenCity)
        );
        assertEquals(expected, actual);

        verifyReplicationsFor(actual);
    }

    @Test
    public void addressesShouldNotBeSavedBecauseOfUniqueViolation() {
        List<Address> givenAddresses = List.of(
                Address.builder().country("Belarus").city("Minsk").build(),
                Address.builder().country("Russia").city("Moscow").build()
        );

        executeExpectingUniqueViolation(() -> addressService.saveAll(givenAddresses));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void personsShouldNotBeSavedBecauseOfForeignKeyViolation() {
        List<Person> givenPersons = List.of(
                Person.builder()
                        .name("Avdifaks")
                        .surname("Kuznetsov")
                        .patronymic("Vasilievich")
                        .birthDate(LocalDate.of(1995, 7, 2))
                        .address(Address.builder().id(255L).build())
                        .build(),
                Person.builder()
                        .name("Harry")
                        .surname("Potter")
                        .patronymic("Sergeevich")
                        .birthDate(LocalDate.of(1990, 8, 4))
                        .address(Address.builder().id(254L).build())
                        .build()
        );

        executeExpectingForeignKeyViolation(() -> personService.saveAll(givenPersons));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeDeletedById() {
        Long givenId = 262L;

        executeWaitingReplication(() -> addressService.delete(givenId), 1, 0, true);

        assertFalse(addressService.isExist(givenId));
        assertFalse(replicatedAddressRepository.existsById(givenId));
    }

    @Test
    public void addressShouldNotBeDeletedByIdBecauseOfForeignKeyViolation() {
        Long givenId = 255L;

        executeExpectingForeignKeyViolation(() -> addressService.delete(givenId));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressShouldBeDeleted() {
        Long givenId = 262L;
        Address givenAddress = Address.builder().id(givenId).build();

        executeWaitingReplication(() -> addressService.delete(givenAddress), 1, 0, true);

        assertFalse(addressService.isExist(givenId));
        assertFalse(replicatedAddressRepository.existsById(givenId));
    }

    @Test
    public void addressShouldNotBeDeletedBecauseOfForeignKeyViolation() {
        Long givenId = 255L;
        Address givenAddress = Address.builder().id(givenId).build();

        executeExpectingForeignKeyViolation(() -> addressService.delete(givenAddress));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressesShouldBeDeletedByIds() {
        Long firstGivenId = 262L;
        Long secondGivenId = 263L;
        Iterable<Long> givenIds = List.of(firstGivenId, secondGivenId);

        executeWaitingReplication(() -> addressService.deleteByIds(givenIds), 2, 0, true);

        assertFalse(addressService.isExist(firstGivenId));
        assertFalse(addressService.isExist(secondGivenId));
        assertFalse(replicatedAddressRepository.existsById(firstGivenId));
        assertFalse(replicatedAddressRepository.existsById(secondGivenId));
    }

    @Test
    public void addressesShouldNotBeDeletedByIdsBecauseOfForeignKeyViolation() {
        Long firstGivenId = 262L;
        Long secondGivenId = 255L;
        Iterable<Long> givenIds = List.of(firstGivenId, secondGivenId);

        executeExpectingForeignKeyViolation(() -> addressService.deleteByIds(givenIds));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    public void addressesShouldBeDeleted() {
        Long firstGivenId = 262L;
        Long secondGivenId = 263L;
        Collection<Address> givenAddresses = List.of(
                Address.builder().id(firstGivenId).build(),
                Address.builder().id(secondGivenId).build()
        );

        executeWaitingReplication(() -> addressService.delete(givenAddresses), 2, 0, true);

        assertFalse(addressService.isExist(firstGivenId));
        assertFalse(addressService.isExist(secondGivenId));
        assertFalse(replicatedAddressRepository.existsById(firstGivenId));
        assertFalse(replicatedAddressRepository.existsById(secondGivenId));
    }

    @Test
    public void addressesShouldNotBeDeletedBecauseOfForeignKeyViolation() {
        Long firstGivenId = 262L;
        Long secondGivenId = 255L;
        Collection<Address> givenAddresses = List.of(
                Address.builder().id(firstGivenId).build(),
                Address.builder().id(secondGivenId).build()
        );

        executeExpectingForeignKeyViolation(() -> addressService.delete(givenAddresses));

        verifyNoReplicationRepositoryMethodCall();
    }

    @Test
    @Sql(
            statements = {
                    "DELETE FROM replicated_persons",
                    "DELETE FROM replicated_addresses",
                    "DELETE FROM persons",
                    "DELETE FROM addresses WHERE id NOT IN (262, 263)",
            }
    )
    public void allAddressesShouldBeDeleted() {
        executeWaitingReplication(() -> addressService.deleteAll(), 2, 0, true);

        assertFalse(addressService.isExist(262L));
        assertFalse(addressService.isExist(263L));
        assertFalse(replicatedAddressRepository.existsById(262L));
        assertFalse(replicatedAddressRepository.existsById(263L));
    }

    @Test
    public void allAddressesShouldNotBeDeletedBecauseOfForeignKeyViolation() {
        executeExpectingForeignKeyViolation(() -> addressService.deleteAll());
    }

    @Test
    public void operationsShouldBeExecuted() {
        executeWaitingReplication(
                () -> {
                    addressService.saveAll(
                            List.of(
                                    Address.builder().country("China").city("Hong Kong").build(),
                                    Address.builder().country("China").city("Anqing").build(),
                                    Address.builder().country("China").city("Bozhou").build()
                            )
                    );
                    personService.saveAll(
                            List.of(
                                    Person.builder()
                                            .name("Avdifaks")
                                            .surname("Kuznetsov")
                                            .patronymic("Vasilievich")
                                            .birthDate(LocalDate.of(1995, 7, 2))
                                            .address(Address.builder().id(1L).build())
                                            .build(),
                                    Person.builder()
                                            .name("Vitenka")
                                            .surname("Kozar")
                                            .patronymic("Vadimovich")
                                            .birthDate(LocalDate.of(1996, 6, 1))
                                            .address(Address.builder().id(2L).build())
                                            .build(),
                                    Person.builder()
                                            .name("Yury")
                                            .surname("Sitnikov")
                                            .patronymic("Stepanovich")
                                            .birthDate(LocalDate.of(1997, 8, 3))
                                            .address(Address.builder().id(3L).build())
                                            .build()
                            )
                    );
                    addressService.save(Address.builder().country("China").city("Huainan").build());
                    executeExpectingUniqueViolation(
                            () -> addressService.save(Address.builder().country("China").city("Huainan").build())
                    );
                    executeExpectingUniqueViolation(
                            () -> addressService.save(Address.builder().country("Russia").city("Moscow").build())
                    );
                    addressService.save(new Address(4L, "Belarus", "Gomel"));
                    personService.save(
                            new Person(
                                    2L,
                                    "Ivan",
                                    "Zuev",
                                    "Ivanovich",
                                    LocalDate.of(1996, 6, 1),
                                    Address.builder().id(2L).build()
                            )
                    );
                    personService.delete(MAX_VALUE);
                    addressService.delete(MAX_VALUE);
                    executeExpectingUniqueViolation(
                            () -> addressService.save(new Address(256L, "Russia", "Moscow"))
                    );
                    personService.delete(259L);
                    addressService.delete(257L);
                    personService.save(
                            new Person(
                                    257L,
                                    "Alexandr",
                                    "Verbitskiy",
                                    "Dmitrievich",
                                    LocalDate.of(2000, 5, 20),
                                    Address.builder().id(256L).build()
                            )
                    );
                    return empty();
                },
                7,
                7,
                false
        );

        verifyEntities(
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
                        new PersonEntity(
                                1L,
                                "Avdifaks",
                                "Kuznetsov",
                                "Vasilievich",
                                LocalDate.of(1995, 7, 2),
                                AddressEntity.builder().id(1L).build()
                        ),
                        new PersonEntity(
                                2L,
                                "Ivan",
                                "Zuev",
                                "Ivanovich",
                                LocalDate.of(1996, 6, 1),
                                AddressEntity.builder().id(2L).build()
                        ),
                        new PersonEntity(
                                3L,
                                "Yury",
                                "Sitnikov",
                                "Stepanovich",
                                LocalDate.of(1997, 8, 3),
                                AddressEntity.builder().id(3L).build()
                        ),
                        new PersonEntity(
                                255L,
                                "Vlad",
                                "Zuev",
                                "Sergeevich",
                                LocalDate.of(2000, 2, 18),
                                AddressEntity.builder().id(255L).build()
                        ),
                        new PersonEntity(
                                256L,
                                "Vasilii",
                                "Dolzhikov",
                                "Borisovich",
                                LocalDate.of(1980, 3, 15),
                                AddressEntity.builder().id(255L).build()
                        ),
                        new PersonEntity(
                                257L,
                                "Alexandr",
                                "Verbitskiy",
                                "Dmitrievich",
                                LocalDate.of(2000, 5, 20),
                                AddressEntity.builder().id(256L).build()
                        ),
                        new PersonEntity(
                                258L,
                                "Pashenka",
                                "Kornev",
                                "Filippovich",
                                LocalDate.of(1995, 4, 23),
                                AddressEntity.builder().id(256L).build()
                        )
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
                        new ReplicatedPersonEntity(
                                1L,
                                "Avdifaks",
                                "Kuznetsov",
                                LocalDate.of(1995, 7, 2),
                                ReplicatedAddressEntity.builder().id(1L).build()
                        ),
                        new ReplicatedPersonEntity(
                                2L,
                                "Ivan",
                                "Zuev",
                                LocalDate.of(1996, 6, 1),
                                ReplicatedAddressEntity.builder().id(2L).build()
                        ),
                        new ReplicatedPersonEntity(
                                3L,
                                "Yury",
                                "Sitnikov",
                                LocalDate.of(1997, 8, 3),
                                ReplicatedAddressEntity.builder().id(3L).build()
                        ),
                        new ReplicatedPersonEntity(
                                255L,
                                "Vlad",
                                "Zuev",
                                LocalDate.of(2000, 2, 18),
                                ReplicatedAddressEntity.builder().id(255L).build()
                        ),
                        new ReplicatedPersonEntity(
                                256L,
                                "Vasilii",
                                "Dolzhikov",
                                LocalDate.of(1980, 3, 15),
                                ReplicatedAddressEntity.builder().id(255L).build()
                        ),
                        new ReplicatedPersonEntity(
                                257L,
                                "Alexandr",
                                "Verbitskiy",
                                LocalDate.of(2000, 5, 20),
                                ReplicatedAddressEntity.builder().id(256L).build()
                        ),
                        new ReplicatedPersonEntity(
                                258L,
                                "Pashenka",
                                "Kornev",
                                LocalDate.of(1995, 4, 23),
                                ReplicatedAddressEntity.builder().id(256L).build()
                        ),
                        new ReplicatedPersonEntity(
                                260L,
                                "Nikolay",
                                "Radoman",
                                LocalDate.of(2000, 2, 20),
                                ReplicatedAddressEntity.builder().id(258L).build()
                        )
                )
        );
    }

    //    @Test
//    public void addressShouldBeDeletedButReplicatedAddressShouldNotBecauseOfForeignKeyViolation() {
//        Long givenId = 258L;
//
//        executeWaitingReplication(() -> deleteAddress(givenId), retryConsumeProperty.getMaxAttempts(), 0, true);
//
//        assertFalse(isAddressExist(givenId));
//        assertTrue(replicatedAddressRepository.existsById(givenId));
//
//        verify(replicatedAddressRepository, times(retryConsumeProperty.getMaxAttempts())).deleteById(eq(givenId));
//    }
//
//    @Test
//    public void personShouldBeSavedButNotReplicatedBecauseOfForeignKeyViolation() {
//        PERSON givenPerson = createPerson("Petr", "Ivanov", "Petrovich", LocalDate.of(2000, 3, 19), 264L);
//
//        PERSON actual = executeWaitingReplication(() -> save(givenPerson), 0, retryConsumeProperty.getMaxAttempts(), true);
//        PERSON expected = createPerson(
//                1L,
//                givenPerson.getName(),
//                givenPerson.getSurname(),
//                givenPerson.getPatronymic(),
//                givenPerson.getBirthDate(),
//                createAddress(264L)
//        );
//        assertEquals(expected, actual);
//
//        assertFalse(replicatedPersonRepository.existsById(actual.getId()));
//
//        verify(
//                replicatedPersonRepository,
//                times(retryConsumeProperty.getMaxAttempts())
//        ).save(any(ReplicatedPersonEntity.class));
//    }
//
//    @Test
//    public void addressShouldBeSavedButNotReplicatedBecauseOfUniqueConstraint() {
//        ADDRESS givenAddress = createAddress("Japan", "Tokyo");
//
//        ADDRESS actual = executeWaitingReplication(() -> save(givenAddress), 1, 0, true);
//        ADDRESS expected = createAddress(1L, givenAddress.getCountry(), givenAddress.getCity());
//        assertEquals(expected, actual);
//
//        assertFalse(replicatedAddressRepository.existsById(actual.getId()));
//        verify(replicatedAddressRepository, times(1)).save(any(ReplicatedAddressEntity.class));
//    }
//
//    @Test
//    public void addressShouldNotBeDeletedBecauseOfTransactionRollBacked() {
//        Long givenId = 262L;
//
//        deleteAddressInRollBackedTransaction(givenId);
//
//        assertTrue(isAddressExist(givenId));
//        assertTrue(replicatedAddressRepository.existsById(givenId));
//    }
//
//    protected abstract ADDRESS createAddress(Long id, String country, String city);
//
//    protected abstract PERSON createPerson(Long id,
//                                           String name,
//                                           String surname,
//                                           String patronymic,
//                                           LocalDate birthDate,
//                                           ADDRESS address);
//
//    protected abstract ADDRESS save(ADDRESS address);
//
//    protected abstract PERSON save(PERSON person);
//
//    protected abstract List<ADDRESS> saveAddresses(List<ADDRESS> addresses);
//
//    @SuppressWarnings("UnusedReturnValue")
//    protected abstract List<PERSON> savePersons(List<PERSON> persons);
//
//    protected abstract ADDRESS update(ADDRESS address);
//
//    @SuppressWarnings("UnusedReturnValue")
//    protected abstract PERSON update(PERSON person);
//
//    protected abstract ADDRESS updateAddressPartial(Long id, Object partial);
//
//    @SuppressWarnings("UnusedReturnValue")
//    protected abstract PERSON updatePersonPartial(Long id, Object partial);
//
//    protected abstract void deleteAddress(Long id);
//
//    protected abstract void deletePerson(Long id);
//
//    protected abstract boolean isAddressExist(Long id);
//
//    protected final ADDRESS createAddress(Long id) {
//        return createAddress(id, null, null);
//    }
//
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
//
//    private ADDRESS createAddress(String country, String city) {
//        return createAddress(null, country, city);
//    }
//
//    private PERSON createPerson(String name,
//                                String surname,
//                                String patronymic,
//                                LocalDate birthDate,
//                                Long addressId) {
//        return createPerson(null, name, surname, patronymic, birthDate, addressId);
//    }
//
//    private PERSON createPerson(Long id,
//                                String name,
//                                String surname,
//                                String patronymic,
//                                LocalDate birthDate,
//                                Long addressId) {
//        ADDRESS address = createAddress(addressId);
//        return createPerson(id, name, surname, patronymic, birthDate, address);
//    }

    private <R> R executeWaitingReplication(Supplier<R> operation,
                                            int addressCalls,
                                            int personCalls,
                                            boolean failedCallsCounted) {
        replicationDeliveryBarrier.expect(addressCalls, personCalls, failedCallsCounted);
        R result = operation.get();
        replicationDeliveryBarrier.await();
        return result;
    }

    private void verifyReplicationFor(Address address) {
        verifyReplicationsFor(singletonList(address));
    }

    private void verifyReplicationsFor(List<Address> addresses) {
        List<Long> ids = mapToIds(addresses);
        List<ReplicatedAddressEntity> actual = findReplicatedAddressesOrderedById(ids);
        List<ReplicatedAddressEntity> expected = mapToReplicatedAddresses(addresses);
        assertEquals(expected.size(), actual.size());
        IntStream.range(0, expected.size()).forEach(i -> ReplicatedAddressEntityUtil.checkEquals(expected.get(i), actual.get(i)));
//        ReplicatedAddressEntityUtil.checkEquals(expected, actual);
    }

    private List<Long> mapToIds(List<Address> addresses) {
        return addresses.stream()
                .map(Address::getId)
                .toList();
    }

    private List<ReplicatedAddressEntity> findReplicatedAddressesOrderedById(List<Long> ids) {
        return entityManager.createQuery("SELECT e FROM ReplicatedAddressEntity e WHERE e.id IN :ids ORDER BY e.id", ReplicatedAddressEntity.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    private List<ReplicatedAddressEntity> mapToReplicatedAddresses(List<Address> addresses) {
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

    private void verifyEntities(List<AddressEntity> expectedAddresses,
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
        assertEquals(expected.size(), actual.size());
        IntStream.range(0, expected.size()).forEach(i -> equalChecker.accept(expected.get(i), actual.get(i)));
//        EntityUtil.checkEquals(expected, actual, equalChecker);
    }

    private void verifyNoReplicationRepositoryMethodCall() {
        verifyNoInteractions(replicatedAddressRepository, replicatedPersonRepository);
    }

//    private void deleteAddressInRollBackedTransaction(Long id) {
//        transactionTemplate.execute(
//                status -> {
//                    status.setRollbackOnly();
//                    deleteAddress(id);
//                    return empty();
//                }
//        );
//    }
//
//    private PersonEntity createPersonEntity(Long id,
//                                            String name,
//                                            String surname,
//                                            String patronymic,
//                                            LocalDate birthDate,
//                                            Long addressId) {
//        return new PersonEntity(id, name, surname, patronymic, birthDate, createAddressEntity(addressId));
//    }
//
//    private AddressEntity createAddressEntity(Long id) {
//        return AddressEntity.builder()
//                .id(id)
//                .build();
//    }
//
//    private ReplicatedAddressEntity createReplicatedAddress(Long id) {
//        return ReplicatedAddressEntity.builder()
//                .id(id)
//                .build();
//    }
//
//    private ReplicatedPersonEntity createReplicatedPerson(Long id,
//                                                          String name,
//                                                          String surname,
//                                                          LocalDate birthDate,
//                                                          Long addressId) {
//        return new ReplicatedPersonEntity(id, name, surname, birthDate, createReplicatedAddress(addressId));
//    }

    @Aspect
    @Component
    public static class ReplicationDeliveryBarrier {
        private static final long TIMEOUT_DELTA_MS = 20000;
        private static final CountDownLatch DEFAULT_LATCH = new CountDownLatch(0);

        private final long timeoutMs;
        private volatile CountDownLatch addressLatch;
        private volatile CountDownLatch personLatch;
        private volatile boolean failedCallsCounted;

        public ReplicationDeliveryBarrier(ReplicationRetryConsumeProperty retryProperty) {
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
