package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
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
    private static final String GIVEN_TOPIC_NAME = "test-topic";

    @Mock
    private KafkaTemplate<Object, ProducedReplication<Object>> mockedKafkaTemplate;

    private ReplicationProducer<Object> producer;

    @Captor
    private ArgumentCaptor<ProducerRecord<Object, ProducedReplication<Object>>> recordCaptor;

    @BeforeEach
    public void initializeProducer() {
        producer = new TestReplicationProducer(mockedKafkaTemplate, GIVEN_TOPIC_NAME);
    }

    @Test
    public void replicationShouldBeSent() {
        Long givenEntityId = 255L;

        producer.produceSave(givenEntityId);

        verify(mockedKafkaTemplate, times(1)).send(recordCaptor.capture());
        ProducerRecord<Object, ProducedReplication<Object>> actual = recordCaptor.getValue();
        var expected = new ProducerRecord<>(GIVEN_TOPIC_NAME, givenEntityId, new TestProducedReplication(givenEntityId));
        assertEquals(expected, actual);
    }

    private static final class TestProducedReplication extends ProducedReplication<Object> {

        public TestProducedReplication(Object body) {
            super(body);
        }

        @Override
        protected Object getEntityIdInternal(Object body) {
            return body;
        }
    }

    private static final class TestReplicationProducer extends ReplicationProducer<Object> {

        public TestReplicationProducer(KafkaTemplate<Object, ProducedReplication<Object>> kafkaTemplate,
                                       String topicName) {
            super(kafkaTemplate, topicName);
        }

        @Override
        protected Object getEntityId(Object model) {
            return model;
        }

        @Override
        protected Object createBody(Object model) {
            return model;
        }

        @Override
        protected ProducedReplication<Object> createReplication(Object body) {
            return new TestProducedReplication(body);
        }
    }
}
