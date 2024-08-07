package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.aop.ProducingReplicationAspect.ReplicationCallback;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.producer.DeleteReplicationProducer;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.producer.SaveReplicationProducer;
import by.aurorasoft.replicator.registry.replicationproducer.DeleteReplicationProducerRegistry;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.registry.SaveReplicationProducerRegistry;
import by.aurorasoft.replicator.testentity.TestEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;

import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

public final class ProducingReplicationAspectTest extends AbstractSpringBootTest {

    @MockBean
    private SaveReplicationProducerRegistry mockedSaveProducerRegistry;

    @MockBean
    private DeleteReplicationProducerRegistry mockedDeleteProducerRegistry;

    @Autowired
    private JpaRepository<TestEntity, Long> repository;

    private MockedStatic<TransactionSynchronizationManager> mockedTransactionManager;

    @Captor
    private ArgumentCaptor<ReplicationCallback> callbackCaptor;

    @BeforeEach
    public void mockTransactionManager() {
        mockedTransactionManager = mockStatic(TransactionSynchronizationManager.class);
    }

    @AfterEach
    public void closeTransactionManager() {
        mockedTransactionManager.close();
    }

    @Test
    public void saveShouldBeProduced() {
        TestEntity givenEntity = TestEntity.builder()
                .id(255L)
                .build();
        SaveReplicationProducer givenProducer = mockSaveProducerForRepository();

        TestEntity actual = repository.save(givenEntity);
        assertSame(givenEntity, actual);

        verifyCallbacks(new ReplicationCallback(givenProducer, givenEntity));
    }

    @Test
    public void saveShouldNotBeProduced() {
        TestEntity givenEntity = TestEntity.builder()
                .id(255L)
                .build();

        TestEntity actual = repository.save(givenEntity);
        assertSame(givenEntity, actual);

        verifyNoCallbacks();
    }

    @Test
    public void saveAndFlushShouldBeProduced() {
        TestEntity givenEntity = TestEntity.builder()
                .id(255L)
                .build();
        SaveReplicationProducer givenProducer = mockSaveProducerForRepository();

        TestEntity actual = repository.saveAndFlush(givenEntity);
        assertSame(givenEntity, actual);

        verifyCallbacks(new ReplicationCallback(givenProducer, givenEntity));
    }

    @Test
    public void saveAndFlushShouldNotBeProduced() {
        TestEntity givenEntity = TestEntity.builder()
                .id(255L)
                .build();

        TestEntity actual = repository.saveAndFlush(givenEntity);
        assertSame(givenEntity, actual);

        verifyNoCallbacks();
    }

    @Test
    public void saveAllShouldBeProduced() {
        TestEntity firstGivenEntity = TestEntity.builder()
                .id(255L)
                .build();
        TestEntity secondGivenEntity = TestEntity.builder()
                .id(256L)
                .build();
        List<TestEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);
        SaveReplicationProducer givenProducer = mockSaveProducerForRepository();

        List<TestEntity> actual = repository.saveAll(givenEntities);
        assertEquals(givenEntities, actual);

        verifyCallbacks(
                new ReplicationCallback(givenProducer, firstGivenEntity),
                new ReplicationCallback(givenProducer, secondGivenEntity)
        );
    }

    @Test
    public void saveAllShouldNotBeProduced() {
        TestEntity firstGivenEntity = TestEntity.builder()
                .id(255L)
                .build();
        TestEntity secondGivenEntity = TestEntity.builder()
                .id(256L)
                .build();
        List<TestEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);

        List<TestEntity> actual = repository.saveAll(givenEntities);
        assertEquals(givenEntities, actual);

        verifyNoCallbacks();
    }

    @Test
    public void saveAllAndFlushShouldBeProduced() {
        TestEntity firstGivenEntity = TestEntity.builder()
                .id(255L)
                .build();
        TestEntity secondGivenEntity = TestEntity.builder()
                .id(256L)
                .build();
        List<TestEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);
        SaveReplicationProducer givenProducer = mockSaveProducerForRepository();

        List<TestEntity> actual = repository.saveAllAndFlush(givenEntities);
        assertEquals(givenEntities, actual);

        verifyCallbacks(
                new ReplicationCallback(givenProducer, firstGivenEntity),
                new ReplicationCallback(givenProducer, secondGivenEntity)
        );
    }

    @Test
    public void saveAllAndFlushShouldNotBeProduced() {
        TestEntity firstGivenEntity = TestEntity.builder()
                .id(255L)
                .build();
        TestEntity secondGivenEntity = TestEntity.builder()
                .id(256L)
                .build();
        List<TestEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);

        List<TestEntity> actual = repository.saveAllAndFlush(givenEntities);
        assertEquals(givenEntities, actual);

        verifyNoCallbacks();
    }

    @Test
    public void deleteByIdShouldBeProduced() {
        Long givenId = 255L;
        DeleteReplicationProducer givenProducer = mockDeleteProducerForRepository();

        repository.deleteById(givenId);

        verifyCallbacks(new ReplicationCallback(givenProducer, givenId));
    }

    @Test
    public void deleteByIdShouldNotBeProduced() {
        Long givenId = 255L;

        repository.deleteById(givenId);

        verifyNoCallbacks();
    }

    @Test
    public void deleteShouldBeProduced() {
        Long givenId = 255L;
        TestEntity givenEntity = TestEntity.builder()
                .id(givenId)
                .build();
        DeleteReplicationProducer givenProducer = mockDeleteProducerForRepository();

        repository.delete(givenEntity);

        verifyCallbacks(new ReplicationCallback(givenProducer, givenId));
    }

    @Test
    public void deleteShouldNotBeProduced() {
        TestEntity givenEntity = TestEntity.builder()
                .id(255L)
                .build();

        repository.delete(givenEntity);

        verifyNoCallbacks();
    }

    @Test
    public void deleteByIdsShouldBeProduced() {
        Long firstGivenId = 255L;
        Long secondGivenId = 256L;
        Long thirdGivenId = 257L;
        Iterable<Long> givenIds = List.of(firstGivenId, secondGivenId, thirdGivenId);
        DeleteReplicationProducer givenProducer = mockDeleteProducerForRepository();

        repository.deleteAllById(givenIds);

        verifyCallbacks(
                new ReplicationCallback(givenProducer, firstGivenId),
                new ReplicationCallback(givenProducer, secondGivenId),
                new ReplicationCallback(givenProducer, thirdGivenId)
        );
    }

    @Test
    public void deleteByIdsShouldNotBeProduced() {
        Iterable<Long> givenIds = List.of(255L, 256L, 257L);

        repository.deleteAllById(givenIds);

        verifyNoCallbacks();
    }

    @Test
    public void deleteByIdsInBatchShouldBeProduced() {
        Long firstGivenId = 255L;
        Long secondGivenId = 256L;
        Long thirdGivenId = 257L;
        Iterable<Long> givenIds = List.of(firstGivenId, secondGivenId, thirdGivenId);
        DeleteReplicationProducer givenProducer = mockDeleteProducerForRepository();

        repository.deleteAllByIdInBatch(givenIds);

        verifyCallbacks(
                new ReplicationCallback(givenProducer, firstGivenId),
                new ReplicationCallback(givenProducer, secondGivenId),
                new ReplicationCallback(givenProducer, thirdGivenId)
        );
    }

    @Test
    public void deleteByIdsInBatchShouldNotBeProduced() {
        Iterable<Long> givenIds = List.of(255L, 256L, 257L);

        repository.deleteAllByIdInBatch(givenIds);

        verifyNoCallbacks();
    }

    @Test
    public void deleteIterableShouldBeProduced() {
        Long firstGivenId = 255L;
        Long secondGivenId = 256L;
        Long thirdGivenId = 257L;
        Iterable<TestEntity> givenEntities = List.of(
                TestEntity.builder()
                        .id(firstGivenId)
                        .build(),
                TestEntity.builder()
                        .id(secondGivenId)
                        .build(),
                TestEntity.builder()
                        .id(thirdGivenId)
                        .build()
        );
        DeleteReplicationProducer givenProducer = mockDeleteProducerForRepository();

        repository.deleteAll(givenEntities);

        verifyCallbacks(
                new ReplicationCallback(givenProducer, firstGivenId),
                new ReplicationCallback(givenProducer, secondGivenId),
                new ReplicationCallback(givenProducer, thirdGivenId)
        );
    }

    @Test
    public void deleteIterableShouldNotBeProduced() {
        Iterable<TestEntity> givenEntities = List.of(
                TestEntity.builder()
                        .id(255L)
                        .build(),
                TestEntity.builder()
                        .id(256L)
                        .build(),
                TestEntity.builder()
                        .id(257L)
                        .build()
        );

        repository.deleteAll(givenEntities);

        verifyNoCallbacks();
    }

    @Test
    public void deleteIterableInBatchShouldBeProduced() {
        Long firstGivenId = 255L;
        Long secondGivenId = 256L;
        Long thirdGivenId = 257L;
        Iterable<TestEntity> givenEntities = List.of(
                TestEntity.builder()
                        .id(firstGivenId)
                        .build(),
                TestEntity.builder()
                        .id(secondGivenId)
                        .build(),
                TestEntity.builder()
                        .id(thirdGivenId)
                        .build()
        );
        DeleteReplicationProducer givenProducer = mockDeleteProducerForRepository();

        repository.deleteAllInBatch(givenEntities);

        verifyCallbacks(
                new ReplicationCallback(givenProducer, firstGivenId),
                new ReplicationCallback(givenProducer, secondGivenId),
                new ReplicationCallback(givenProducer, thirdGivenId)
        );
    }

    @Test
    public void deleteIterableInBatchShouldNotBeProduced() {
        Iterable<TestEntity> givenEntities = List.of(
                TestEntity.builder()
                        .id(255L)
                        .build(),
                TestEntity.builder()
                        .id(256L)
                        .build(),
                TestEntity.builder()
                        .id(257L)
                        .build()
        );

        repository.deleteAllInBatch(givenEntities);

        verifyNoCallbacks();
    }

    @Test
    public void deleteAllShouldBeProduced() {
        DeleteReplicationProducer givenProducer = mockDeleteProducerForRepository();

        repository.deleteAll();

        verifyCallbacks(
                new ReplicationCallback(givenProducer, 512L),
                new ReplicationCallback(givenProducer, 513L),
                new ReplicationCallback(givenProducer, 514L)
        );
    }

    @Test
    public void deleteAllShouldNotBeProduced() {
        repository.deleteAll();

        verifyNoCallbacks();
    }

    @Test
    public void deleteAllInBatchShouldBeProduced() {
        DeleteReplicationProducer givenProducer = mockDeleteProducerForRepository();

        repository.deleteAllInBatch();

        verifyCallbacks(
                new ReplicationCallback(givenProducer, 512L),
                new ReplicationCallback(givenProducer, 513L),
                new ReplicationCallback(givenProducer, 514L)
        );
    }

    @Test
    public void deleteAllInBatchShouldNotBeProduced() {
        repository.deleteAllInBatch();

        verifyNoCallbacks();
    }

    private SaveReplicationProducer mockSaveProducerForRepository() {
        return mockProducerForRepository(mockedSaveProducerRegistry, SaveReplicationProducer.class);
    }

    private DeleteReplicationProducer mockDeleteProducerForRepository() {
        return mockProducerForRepository(mockedDeleteProducerRegistry, DeleteReplicationProducer.class);
    }

    private <P extends ReplicationProducer<?>> P mockProducerForRepository(ReplicationProducerRegistry<P> registry,
                                                                           Class<P> producerType) {
        P producer = mock(producerType);
        when(registry.get(same(repository))).thenReturn(Optional.of(producer));
        return producer;
    }

    private void verifyCallbacks(ReplicationCallback... callbacks) {
        mockedTransactionManager
                .verify(() -> registerSynchronization(callbackCaptor.capture()), times(callbacks.length));
        List<ReplicationCallback> actual = callbackCaptor.getAllValues();
        List<ReplicationCallback> expected = List.of(callbacks);
        checkEquals(expected, actual);
    }

    private void verifyNoCallbacks() {
        mockedTransactionManager.verifyNoInteractions();
    }

    private void checkEquals(List<ReplicationCallback> expected, List<ReplicationCallback> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }

    private void checkEquals(ReplicationCallback expected, ReplicationCallback actual) {
        assertSame(expected.getProducer(), actual.getProducer());
        assertEquals(expected.getModel(), actual.getModel());
    }
}
