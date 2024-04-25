//package by.aurorasoft.replicator.producer;
//
//import by.aurorasoft.replicator.base.v2.dto.TestDto;
//import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
//import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.verify;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ReplicationProducerTest {
//    private static final String GIVEN_TOPIC_NAME = "sync-dto";
//
//    @Mock
//    private KafkaTemplate<Long, ProducedReplication<Long>> mockedKafkaTemplate;
//
//    private ReplicationProducer<Long> producer;
//
//    @Captor
//    private ArgumentCaptor<ProducerRecord<Long, ProducedReplication<Long>>> recordArgumentCaptor;
//
//    @Before
//    public void initializeProducer() {
//        producer = new ReplicationProducer<>(GIVEN_TOPIC_NAME, mockedKafkaTemplate);
//    }
//
//    @Test
//    public void replicationShouldBeSent() {
//        final ProducedReplication<Long> givenReplication = new SaveProducedReplication<>(new TestDto(255L));
//
//        producer.send(givenReplication);
//
//        verifyProduce(givenReplication);
//    }
//
//    private void verifyProduce(final ProducedReplication<Long> replication) {
//        verify(mockedKafkaTemplate).send(recordArgumentCaptor.capture());
//        final ProducerRecord<Long, ProducedReplication<Long>> actual = recordArgumentCaptor.getValue();
//        final ProducerRecord<Long, ProducedReplication<Long>> expected = createRecord(replication);
//        assertEquals(expected, actual);
//    }
//
//    private static ProducerRecord<Long, ProducedReplication<Long>> createRecord(final ProducedReplication<Long> replication) {
//        return new ProducerRecord<>(GIVEN_TOPIC_NAME, replication.getEntityId(), replication);
//    }
//}
