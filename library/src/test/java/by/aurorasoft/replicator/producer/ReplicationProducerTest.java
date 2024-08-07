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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerTest {
    private static final String GIVEN_TOPIC_NAME = "test-topic";

    @Mock
    private KafkaTemplate<Object, ProducedReplication<?>> mockedKafkaTemplate;

    private ReplicationProducer<Object> producer;

    @Captor
    private ArgumentCaptor<ProducerRecord<Object, ProducedReplication<?>>> recordCaptor;

    @BeforeEach
    public void initializeProducer() {
        producer = new TestReplicationProducer(GIVEN_TOPIC_NAME, mockedKafkaTemplate);
    }

    @Test
    public void replicationShouldBeSent() {
        Long givenEntityId = 255L;

        producer.send(givenEntityId);

        verify(mockedKafkaTemplate, times(1)).send(recordCaptor.capture());
        ProducerRecord<Object, ProducedReplication<?>> actual = recordCaptor.getValue();
        var expected = new ProducerRecord<>(GIVEN_TOPIC_NAME, givenEntityId, new TestProducedReplication(givenEntityId));
        assertEquals(expected, actual);
    }

    @Test
    public void modelShouldBeConvertedToTransportable() {
        Object givenModel = new Object();

        Object actual = producer.convertModelToTransportable(givenModel);
        assertSame(givenModel, actual);
    }

    @Test
    public void transportableShouldBeConvertedToTopicValue() {
        Object givenTransportable = new Object();

        ProducedReplication<?> actual = producer.convertTransportableToTopicValue(givenTransportable);
        ProducedReplication<?> expected = new TestProducedReplication(givenTransportable);
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

        public TestReplicationProducer(String topicName, KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate) {
            super(topicName, kafkaTemplate);
        }

        @Override
        protected Object getEntityId(Object model) {
            return model;
        }

        @Override
        protected Object createReplicationBody(Object model) {
            return model;
        }

        @Override
        protected ProducedReplication<?> createReplication(Object body) {
            return new TestProducedReplication(body);
        }
    }
}
