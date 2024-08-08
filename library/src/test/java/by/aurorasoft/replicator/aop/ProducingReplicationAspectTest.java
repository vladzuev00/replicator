package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.testentity.TestEntity;
import by.aurorasoft.replicator.transactioncallback.DeleteReplicationTransactionCallback;
import by.aurorasoft.replicator.transactioncallback.ReplicationTransactionCallback;
import by.aurorasoft.replicator.transactioncallback.SaveReplicationTransactionCallback;
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
    private ReplicationProducerRegistry mockedProducerRegistry;

    @Autowired
    private JpaRepository<TestEntity, Long> repository;

    private MockedStatic<TransactionSynchronizationManager> mockedTransactionManager;

    @Captor
    private ArgumentCaptor<ReplicationTransactionCallback> callbackCaptor;

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
        ReplicationProducer givenProducer = mockProducerForRepository();

        TestEntity actual = repository.save(givenEntity);
        assertSame(givenEntity, actual);

        verifyCallbacks(new SaveReplicationTransactionCallback(givenEntity, givenProducer));
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
        ReplicationProducer givenProducer = mockProducerForRepository();

        TestEntity actual = repository.saveAndFlush(givenEntity);
        assertSame(givenEntity, actual);

        verifyCallbacks(new SaveReplicationTransactionCallback(givenEntity, givenProducer));
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
        ReplicationProducer givenProducer = mockProducerForRepository();

        List<TestEntity> actual = repository.saveAll(givenEntities);
        assertEquals(givenEntities, actual);

        verifyCallbacks(
                new SaveReplicationTransactionCallback(firstGivenEntity, givenProducer),
                new SaveReplicationTransactionCallback(secondGivenEntity, givenProducer)
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
        ReplicationProducer givenProducer = mockProducerForRepository();

        List<TestEntity> actual = repository.saveAllAndFlush(givenEntities);
        assertEquals(givenEntities, actual);

        verifyCallbacks(
                new SaveReplicationTransactionCallback(firstGivenEntity, givenProducer),
                new SaveReplicationTransactionCallback(secondGivenEntity, givenProducer)
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
        ReplicationProducer givenProducer = mockProducerForRepository();

        repository.deleteById(givenId);

        verifyCallbacks(new DeleteReplicationTransactionCallback(givenId, givenProducer));
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
        ReplicationProducer givenProducer = mockProducerForRepository();

        repository.delete(givenEntity);

        verifyCallbacks(new DeleteReplicationTransactionCallback(givenId, givenProducer));
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
        ReplicationProducer givenProducer = mockProducerForRepository();

        repository.deleteAllById(givenIds);

        verifyCallbacks(
                new DeleteReplicationTransactionCallback(firstGivenId, givenProducer),
                new DeleteReplicationTransactionCallback(secondGivenId, givenProducer),
                new DeleteReplicationTransactionCallback(thirdGivenId, givenProducer)
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
        ReplicationProducer givenProducer = mockProducerForRepository();

        repository.deleteAllByIdInBatch(givenIds);

        verifyCallbacks(
                new DeleteReplicationTransactionCallback(firstGivenId, givenProducer),
                new DeleteReplicationTransactionCallback(secondGivenId, givenProducer),
                new DeleteReplicationTransactionCallback(thirdGivenId, givenProducer)
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
        ReplicationProducer givenProducer = mockProducerForRepository();

        repository.deleteAll(givenEntities);

        verifyCallbacks(
                new DeleteReplicationTransactionCallback(firstGivenId, givenProducer),
                new DeleteReplicationTransactionCallback(secondGivenId, givenProducer),
                new DeleteReplicationTransactionCallback(thirdGivenId, givenProducer)
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
        ReplicationProducer givenProducer = mockProducerForRepository();

        repository.deleteAllInBatch(givenEntities);

        verifyCallbacks(
                new DeleteReplicationTransactionCallback(firstGivenId, givenProducer),
                new DeleteReplicationTransactionCallback(secondGivenId, givenProducer),
                new DeleteReplicationTransactionCallback(thirdGivenId, givenProducer)
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
        ReplicationProducer givenProducer = mockProducerForRepository();

        repository.deleteAll();

        verifyCallbacks(
                new DeleteReplicationTransactionCallback(512L, givenProducer),
                new DeleteReplicationTransactionCallback(513L, givenProducer),
                new DeleteReplicationTransactionCallback(514L, givenProducer)
        );
    }

    @Test
    public void deleteAllShouldNotBeProduced() {
        repository.deleteAll();

        verifyNoCallbacks();
    }

    @Test
    public void deleteAllInBatchShouldBeProduced() {
        ReplicationProducer givenProducer = mockProducerForRepository();

        repository.deleteAllInBatch();

        verifyCallbacks(
                new DeleteReplicationTransactionCallback(512L, givenProducer),
                new DeleteReplicationTransactionCallback(513L, givenProducer),
                new DeleteReplicationTransactionCallback(514L, givenProducer)
        );
    }

    @Test
    public void deleteAllInBatchShouldNotBeProduced() {
        repository.deleteAllInBatch();

        verifyNoCallbacks();
    }

    private ReplicationProducer mockProducerForRepository() {
        ReplicationProducer producer = mock(ReplicationProducer.class);
        when(mockedProducerRegistry.get(same(repository))).thenReturn(Optional.of(producer));
        return producer;
    }

    private void verifyCallbacks(ReplicationTransactionCallback... callbacks) {
        mockedTransactionManager
                .verify(() -> registerSynchronization(callbackCaptor.capture()), times(callbacks.length));
        List<ReplicationTransactionCallback> actual = callbackCaptor.getAllValues();
        List<ReplicationTransactionCallback> expected = List.of(callbacks);
        checkEquals(expected, actual);
    }

    private void verifyNoCallbacks() {
        mockedTransactionManager.verifyNoInteractions();
    }

    private void checkEquals(List<ReplicationTransactionCallback> expected,
                             List<ReplicationTransactionCallback> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }

    private void checkEquals(ReplicationTransactionCallback expected, ReplicationTransactionCallback actual) {
        assertSame(expected.getClass(), actual.getClass());
        assertEquals(expected.getBody(), actual.getBody());
        assertSame(expected.getProducer(), actual.getProducer());
    }
}
