package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.v2.dto.TestV2Dto;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationProducerTest {
    private static final String GIVEN_TOPIC_NAME = "sync-dto";

    @Mock
    private KafkaTemplate<Object, ProducedReplication> mockedKafkaTemplate;

    private ReplicationProducer producer;

    @Captor
    private ArgumentCaptor<ProducerRecord<Object, ProducedReplication>> recordArgumentCaptor;

    @Before
    public void initializeProducer() {
        producer = new ReplicationProducer(GIVEN_TOPIC_NAME, mockedKafkaTemplate);
    }

    @Test
    public void replicationShouldBeSent() {
        ProducedReplication givenReplication = new SaveProducedReplication(new TestV2Dto(255L));

        producer.send(givenReplication);

        verifyProduce(givenReplication);
    }

    private void verifyProduce(ProducedReplication replication) {
        verify(mockedKafkaTemplate, times(1)).send(recordArgumentCaptor.capture());
        ProducerRecord<Object, ProducedReplication> actual = recordArgumentCaptor.getValue();
        ProducerRecord<Object, ProducedReplication> expected = createRecord(replication);
        assertEquals(expected, actual);
    }

    private ProducerRecord<Object, ProducedReplication> createRecord(ProducedReplication replication) {
        return new ProducerRecord<>(GIVEN_TOPIC_NAME, replication.getEntityId(), replication);
    }
}
