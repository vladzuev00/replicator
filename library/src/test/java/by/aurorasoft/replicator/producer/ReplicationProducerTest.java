package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.view.DtoJsonView;
import by.aurorasoft.replicator.v2.dto.TestV2Dto;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerTest {
    private static final String GIVEN_TOPIC_NAME = "sync-dto";

    @Mock
    private KafkaTemplate<Object, ProducedReplication<?>> mockedKafkaTemplate;

    private ReplicationProducer producer;

    @Captor
    private ArgumentCaptor<ProducerRecord<Object, ProducedReplication<?>>> recordArgumentCaptor;

    @BeforeEach
    public void initializeProducer() {
        producer = new ReplicationProducer(GIVEN_TOPIC_NAME, mockedKafkaTemplate);
    }

    @Test
    public void replicationShouldBeSent() {
        ProducedReplication<?> givenReplication = new SaveProducedReplication(new DtoJsonView<>(new TestV2Dto(255L)));

        producer.send(givenReplication);

        verifyProduce(givenReplication);
    }

    private void verifyProduce(ProducedReplication<?> replication) {
        verify(mockedKafkaTemplate, times(1)).send(recordArgumentCaptor.capture());
        ProducerRecord<Object, ProducedReplication<?>> actual = recordArgumentCaptor.getValue();
        ProducerRecord<Object, ProducedReplication<?>> expected = createRecord(replication);
        assertEquals(expected, actual);
    }

    private ProducerRecord<Object, ProducedReplication<?>> createRecord(ProducedReplication<?> replication) {
        return new ProducerRecord<>(GIVEN_TOPIC_NAME, replication.getEntityId(), replication);
    }
}
