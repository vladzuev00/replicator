//package by.aurorasoft.replicator.aop;
//
//import by.aurorasoft.replicator.aop.ProducingReplicationAspect.ReplicationCallback;
//import by.aurorasoft.replicator.base.AbstractSpringBootTest;
//import by.aurorasoft.replicator.factory.SaveProducedReplicationFactory;
//import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
//import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
//import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
//import by.aurorasoft.replicator.producer.ReplicationProducer;
//import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
//import by.aurorasoft.replicator.testentity.TestEntity;
//import org.aspectj.lang.JoinPoint;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.MockedStatic;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
//import java.util.List;
//import java.util.Optional;
//
//import static by.aurorasoft.replicator.util.ProxyUtil.unProxy;
//import static java.util.Arrays.stream;
//import static java.util.stream.IntStream.range;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;
//
//public final class TEMPProducingReplicationAspectTest extends AbstractSpringBootTest {
//
//    @MockBean
//    private ReplicationProducerRegistry mockedProducerRegistry;
//
//    @MockBean
//    private SaveProducedReplicationFactory mockedSaveReplicationFactory;
//
//    @Autowired
//    private FirstTestRepository repository;
//
//    private MockedStatic<TransactionSynchronizationManager> mockedTransactionManager;
//
//    @Captor
//    private ArgumentCaptor<ReplicationCallback> callbackArgumentCaptor;
//
//    @BeforeEach
//    public void mockTransactionManager() {
//        mockedTransactionManager = mockStatic(TransactionSynchronizationManager.class);
//    }
//
//    @AfterEach
//    public void closeTransactionManager() {
//        mockedTransactionManager.close();
//    }
//
//    @Test
//    public void saveShouldBeProduced() {
//        TestEntity givenEntity = new TestEntity(255L, "first-value", "second-value");
//        SaveProducedReplication givenReplication = mockSaveReplicationFor(givenEntity);
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        TestEntity actual = repository.save(givenEntity);
//        assertSame(givenEntity, actual);
//
//        verifyProducing(givenProducer, givenReplication);
//    }
//
//    @Test
//    public void saveShouldNotBeProducedBecauseOfNoProducer() {
//        TestEntity givenEntity = new TestEntity(255L, "first-value", "second-value");
//
//        assertThrows(IllegalStateException.class, () -> repository.save(givenEntity));
//    }
//
//    @Test
//    public void saveAndFlushShouldBeProduced() {
//        TestEntity givenEntity = new TestEntity(255L, "first-value", "second-value");
//        SaveProducedReplication givenReplication = mockSaveReplicationFor(givenEntity);
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        TestEntity actual = repository.saveAndFlush(givenEntity);
//        assertSame(givenEntity, actual);
//
//        verifyProducing(givenProducer, givenReplication);
//    }
//
//    @Test
//    public void saveAndFlushShouldNotBeProducedBecauseOfNoProducer() {
//        TestEntity givenEntity = new TestEntity(255L, "first-value", "second-value");
//
//        assertThrows(IllegalStateException.class, () -> repository.saveAndFlush(givenEntity));
//    }
//
//    @Test
//    public void saveAllShouldBeProduced() {
//        TestEntity firstGivenEntity = new TestEntity(255L, "first-value", "second-value");
//        TestEntity secondGivenEntity = new TestEntity(256L, "third-value", "fourth-value");
//        List<TestEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);
//        SaveProducedReplication firstGivenReplication = mockSaveReplicationFor(firstGivenEntity);
//        SaveProducedReplication secondGivenReplication = mockSaveReplicationFor(secondGivenEntity);
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        List<TestEntity> actual = repository.saveAll(givenEntities);
//        assertEquals(givenEntities, actual);
//
//        verifyProducing(givenProducer, firstGivenReplication, secondGivenReplication);
//    }
//
//    @Test
//    public void saveAllShouldNotBeProducedBecauseOfNoProducer() {
//        TestEntity firstGivenEntity = new TestEntity(255L, "first-value", "second-value");
//        TestEntity secondGivenEntity = new TestEntity(256L, "third-value", "fourth-value");
//        List<TestEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);
//
//        assertThrows(IllegalStateException.class, () -> repository.saveAll(givenEntities));
//    }
//
//    @Test
//    public void saveAllAndFlushShouldBeProduced() {
//        TestEntity firstGivenEntity = new TestEntity(255L, "first-value", "second-value");
//        TestEntity secondGivenEntity = new TestEntity(256L, "third-value", "fourth-value");
//        List<TestEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);
//        SaveProducedReplication firstGivenReplication = mockSaveReplicationFor(firstGivenEntity);
//        SaveProducedReplication secondGivenReplication = mockSaveReplicationFor(secondGivenEntity);
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        List<TestEntity> actual = repository.saveAllAndFlush(givenEntities);
//        assertEquals(givenEntities, actual);
//
//        verifyProducing(givenProducer, firstGivenReplication, secondGivenReplication);
//    }
//
//    @Test
//    public void saveAllAndFlushShouldNotBeProducedBecauseOfNoProducer() {
//        TestEntity firstGivenEntity = new TestEntity(255L, "first-value", "second-value");
//        TestEntity secondGivenEntity = new TestEntity(256L, "third-value", "fourth-value");
//        List<TestEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);
//
//        assertThrows(IllegalStateException.class, () -> repository.saveAllAndFlush(givenEntities));
//    }
//
//    @Test
//    public void deleteByIdShouldBeProduced() {
//        Long givenId = 255L;
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        repository.deleteById(givenId);
//
//        verifyProducing(givenProducer, new DeleteProducedReplication(givenId));
//    }
//
//    @Test
//    public void deleteByIdShouldNotBeProducedBecauseOfNoProducer() {
//        Long givenId = 255L;
//
//        assertThrows(IllegalStateException.class, () -> repository.deleteById(givenId));
//    }
//
//    @Test
//    public void deleteShouldBeProduced() {
//        Long givenId = 255L;
//        TestEntity givenEntity = TestEntity.builder()
//                .id(givenId)
//                .build();
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        repository.delete(givenEntity);
//
//        verifyProducing(givenProducer, new DeleteProducedReplication(givenId));
//    }
//
//    @Test
//    public void deleteShouldNotBeProducedBecauseOfNoProducer() {
//        TestEntity givenEntity = TestEntity.builder()
//                .id(255L)
//                .build();
//
//        assertThrows(IllegalStateException.class, () -> repository.delete(givenEntity));
//    }
//
//    @Test
//    public void deleteByIdsShouldBeProduced() {
//        Long firstGivenId = 255L;
//        Long secondGivenId = 256L;
//        Long thirdGivenId = 257L;
//        Iterable<Long> givenIds = List.of(firstGivenId, secondGivenId, thirdGivenId);
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        repository.deleteAllById(givenIds);
//
//        verifyProducing(
//                givenProducer,
//                new DeleteProducedReplication(firstGivenId),
//                new DeleteProducedReplication(secondGivenId),
//                new DeleteProducedReplication(thirdGivenId)
//        );
//    }
//
//    @Test
//    public void deleteByIdsShouldNotBeProducedBecauseOfNoProducer() {
//        Iterable<Long> givenIds = List.of(255L, 256L, 257L);
//
//        assertThrows(IllegalStateException.class, () -> repository.deleteAllById(givenIds));
//    }
//
//    @Test
//    public void deleteByIdsInBatchShouldBeProduced() {
//        Long firstGivenId = 255L;
//        Long secondGivenId = 256L;
//        Long thirdGivenId = 257L;
//        Iterable<Long> givenIds = List.of(firstGivenId, secondGivenId, thirdGivenId);
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        repository.deleteAllByIdInBatch(givenIds);
//
//        verifyProducing(
//                givenProducer,
//                new DeleteProducedReplication(firstGivenId),
//                new DeleteProducedReplication(secondGivenId),
//                new DeleteProducedReplication(thirdGivenId)
//        );
//    }
//
//    @Test
//    public void deleteByIdsInBatchShouldNotBeProducedBecauseOfNoProducer() {
//        Iterable<Long> givenIds = List.of(255L, 256L, 257L);
//
//        assertThrows(IllegalStateException.class, () -> repository.deleteAllByIdInBatch(givenIds));
//    }
//
//    @Test
//    public void deleteIterableShouldBeProduced() {
//        Long firstGivenId = 255L;
//        Long secondGivenId = 256L;
//        Long thirdGivenId = 257L;
//        Iterable<TestEntity> givenEntities = List.of(
//                TestEntity.builder()
//                        .id(firstGivenId)
//                        .build(),
//                TestEntity.builder()
//                        .id(secondGivenId)
//                        .build(),
//                TestEntity.builder()
//                        .id(thirdGivenId)
//                        .build()
//        );
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        repository.deleteAll(givenEntities);
//
//        verifyProducing(
//                givenProducer,
//                new DeleteProducedReplication(firstGivenId),
//                new DeleteProducedReplication(secondGivenId),
//                new DeleteProducedReplication(thirdGivenId)
//        );
//    }
//
//    @Test
//    public void deleteIterableShouldNotBeProducedBecauseOfNoProducer() {
//        Iterable<TestEntity> givenEntities = List.of(
//                TestEntity.builder()
//                        .id(255L)
//                        .build(),
//                TestEntity.builder()
//                        .id(256L)
//                        .build(),
//                TestEntity.builder()
//                        .id(257L)
//                        .build()
//        );
//
//        assertThrows(IllegalStateException.class, () -> repository.deleteAll(givenEntities));
//    }
//
//    @Test
//    public void deleteIterableInBatchShouldBeProduced() {
//        Long firstGivenId = 255L;
//        Long secondGivenId = 256L;
//        Long thirdGivenId = 257L;
//        Iterable<TestEntity> givenEntities = List.of(
//                TestEntity.builder()
//                        .id(firstGivenId)
//                        .build(),
//                TestEntity.builder()
//                        .id(secondGivenId)
//                        .build(),
//                TestEntity.builder()
//                        .id(thirdGivenId)
//                        .build()
//        );
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        repository.deleteAllInBatch(givenEntities);
//
//        verifyProducing(
//                givenProducer,
//                new DeleteProducedReplication(firstGivenId),
//                new DeleteProducedReplication(secondGivenId),
//                new DeleteProducedReplication(thirdGivenId)
//        );
//    }
//
//    @Test
//    public void deleteIterableInBatchShouldNotBeProducedBecauseOfNoProducer() {
//        Iterable<TestEntity> givenEntities = List.of(
//                TestEntity.builder()
//                        .id(255L)
//                        .build(),
//                TestEntity.builder()
//                        .id(256L)
//                        .build(),
//                TestEntity.builder()
//                        .id(257L)
//                        .build()
//        );
//
//        assertThrows(IllegalStateException.class, () -> repository.deleteAllInBatch(givenEntities));
//    }
//
//    @Test
//    public void deleteAllShouldBeProduced() {
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        repository.deleteAll();
//
//        verifyProducing(
//                givenProducer,
//                new DeleteProducedReplication(512L),
//                new DeleteProducedReplication(513L),
//                new DeleteProducedReplication(514L)
//        );
//    }
//
//    @Test
//    public void deleteAllShouldNotBeProducedBecauseOfNoProducer() {
//        assertThrows(IllegalStateException.class, () -> repository.deleteAll());
//    }
//
//    @Test
//    public void deleteAllInBatchShouldBeProduced() {
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        repository.deleteAllInBatch();
//
//        verifyProducing(
//                givenProducer,
//                new DeleteProducedReplication(512L),
//                new DeleteProducedReplication(513L),
//                new DeleteProducedReplication(514L)
//        );
//    }
//
//    @Test
//    public void deleteAllInBatchShouldNotBeProducedBecauseOfNoProducer() {
//        assertThrows(IllegalStateException.class, () -> repository.deleteAllInBatch());
//    }
//
//    private SaveProducedReplication mockSaveReplicationFor(TestEntity entity) {
//        SaveProducedReplication replication = mock(SaveProducedReplication.class);
//        when(mockedSaveReplicationFactory.create(same(entity), any(JoinPoint.class))).thenReturn(replication);
//        return replication;
//    }
//
//    private ReplicationProducer mockProducerForRepository() {
//        ReplicationProducer producer = mock(ReplicationProducer.class);
//        when(mockedProducerRegistry.get(same(unProxyRepository()))).thenReturn(Optional.of(producer));
//        return producer;
//    }
//
//    private JpaRepository<?, ?> unProxyRepository() {
//        return (JpaRepository<?, ?>) unProxy(repository);
//    }
//
//    private void verifyProducing(ReplicationProducer producer, ProducedReplication<?>... replications) {
//        mockedTransactionManager
//                .verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(replications.length));
//        List<ReplicationCallback> actual = callbackArgumentCaptor.getAllValues();
//        List<ReplicationCallback> expected = createCallbacks(producer, replications);
//        checkEquals(expected, actual);
//    }
//
//    private List<ReplicationCallback> createCallbacks(ReplicationProducer producer,
//                                                      ProducedReplication<?>... replications) {
//        return stream(replications)
//                .map(replication -> new ReplicationCallback(producer, replication))
//                .toList();
//    }
//
//    private void checkEquals(List<ReplicationCallback> expected, List<ReplicationCallback> actual) {
//        assertEquals(expected.size(), actual.size());
//        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
//    }
//
//    private void checkEquals(ReplicationCallback expected, ReplicationCallback actual) {
//        assertSame(expected.getProducer(), actual.getProducer());
//        assertEquals(expected.getReplication(), actual.getReplication());
//    }
//}
