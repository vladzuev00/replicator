//package by.aurorasoft.replicator.consuming.consumer;
//
//import by.aurorasoft.replicator.base.entity.TestEntity;
//import by.aurorasoft.replicator.consuming.ReplicationConsumingPipelineStarter;
//import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import static java.lang.Integer.MIN_VALUE;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ReplicationConsumerTest {
//    private static final String GIVEN_TOPIC = "sync-dto";
//
//    @Mock
//    private JpaRepository<TestEntity, Long> mockedRepository;
//
//    private ReplicationConsumingPipelineStarter<Long, TestEntity> consumer;
//
//    @Before
//    public void initializeConsumer() {
//        consumer = new ReplicationConsumingPipelineStarter<>(
//                null,
//                GIVEN_TOPIC,
//                null,
//                null,
//                mockedRepository
//        );
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void recordShouldBeListened() {
//        final ConsumedReplication<Long, TestEntity> givenReplication = mock(ConsumedReplication.class);
//        final ConsumerRecord<Long, ConsumedReplication<Long, TestEntity>> givenRecord = createRecord(
//                255L,
//                givenReplication
//        );
//
//        consumer.start(givenRecord);
//
//        verify(givenReplication, times(1)).execute(mockedRepository);
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static ConsumerRecord<Long, ConsumedReplication<Long, TestEntity>> createRecord(
//            final Long id,
//            final ConsumedReplication<Long, TestEntity> replication
//    ) {
//        return new ConsumerRecord<>(GIVEN_TOPIC, MIN_VALUE, MIN_VALUE, id, replication);
//    }
//}
