//package by.aurorasoft.replicator.aop;
//
//import by.aurorasoft.replicator.base.AbstractSpringBootTest;
//import by.aurorasoft.replicator.producer.ReplicationProducer;
//import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
//import by.aurorasoft.replicator.testentity.TestEntity;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.Mockito.*;
//
//public final class ProducingReplicationAspectTest extends AbstractSpringBootTest {
//
//    @MockBean
//    private ReplicationProducerRegistry mockedProducerRegistry;
//
//    @Autowired
//    private JpaRepository<TestEntity, Long> repository;
//
//    @Test
//    public void saveShouldBeProduced() {
//        TestEntity givenEntity = TestEntity.builder()
//                .id(255L)
//                .build();
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        TestEntity actual = repository.save(givenEntity);
//        assertSame(givenEntity, actual);
//
//        verify(givenProducer, times(1)).produceSaveAfterCommit(same(givenEntity));
//    }
//
//    @Test
//    public void saveAndFlushShouldBeProduced() {
//        TestEntity givenEntity = TestEntity.builder()
//                .id(255L)
//                .build();
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        TestEntity actual = repository.saveAndFlush(givenEntity);
//        assertSame(givenEntity, actual);
//
//        verify(givenProducer, times(1)).produceSaveAfterCommit(same(givenEntity));
//    }
//
//    @Test
//    public void saveAllShouldBeProduced() {
//        TestEntity firstGivenEntity = TestEntity.builder()
//                .id(255L)
//                .build();
//        TestEntity secondGivenEntity = TestEntity.builder()
//                .id(256L)
//                .build();
//        List<TestEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        List<TestEntity> actual = repository.saveAll(givenEntities);
//        assertEquals(givenEntities, actual);
//
//        verify(givenProducer, times(1)).produceSaveAfterCommit(same(firstGivenEntity));
//        verify(givenProducer, times(1)).produceSaveAfterCommit(same(secondGivenEntity));
//    }
//
//    @Test
//    public void saveAllAndFlushShouldBeProduced() {
//        TestEntity firstGivenEntity = TestEntity.builder()
//                .id(255L)
//                .build();
//        TestEntity secondGivenEntity = TestEntity.builder()
//                .id(256L)
//                .build();
//        List<TestEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        List<TestEntity> actual = repository.saveAllAndFlush(givenEntities);
//        assertEquals(givenEntities, actual);
//
//        verify(givenProducer, times(1)).produceSaveAfterCommit(same(firstGivenEntity));
//        verify(givenProducer, times(1)).produceSaveAfterCommit(same(secondGivenEntity));
//    }
//
//    @Test
//    public void deleteByIdShouldBeProduced() {
//        Long givenId = 255L;
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        repository.deleteById(givenId);
//
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(givenId));
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
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(givenId));
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
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(firstGivenId));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(secondGivenId));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(thirdGivenId));
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
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(firstGivenId));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(secondGivenId));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(thirdGivenId));
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
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(firstGivenId));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(secondGivenId));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(thirdGivenId));
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
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(firstGivenId));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(secondGivenId));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(thirdGivenId));
//    }
//
//    @Test
//    public void deleteAllShouldBeProduced() {
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        repository.deleteAll();
//
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(eq(512L));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(eq(513L));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(eq(514L));
//    }
//
//    @Test
//    public void deleteAllInBatchShouldBeProduced() {
//        ReplicationProducer givenProducer = mockProducerForRepository();
//
//        repository.deleteAllInBatch();
//
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(eq(512L));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(eq(513L));
//        verify(givenProducer, times(1)).produceDeleteAfterCommit(eq(514L));
//    }
//
//    private ReplicationProducer mockProducerForRepository() {
//        ReplicationProducer producer = mock(ReplicationProducer.class);
//        when(mockedProducerRegistry.getByRepository(same(repository))).thenReturn(Optional.of(producer));
//        return producer;
//    }
//}
