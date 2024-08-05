package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.producer.KafkaReplicationProducer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerFactoryTest {
    private static final String FIELD_NAME_TOPIC_NAME = "topicName";
    private static final String FIELD_NAME_KAFKA_TEMPLATE = "kafkaTemplate";

    @Mock
    private ReplicationProducerConfigsFactory mockedConfigsFactory;

    @Mock
    private ReplicationProducerKeySerializerFactory mockedKeySerializerFactory;

    @Mock
    private ReplicationProducerValueSerializerFactory mockedValueSerializerFactory;

    private ReplicationProducerFactory producerFactory;

    @BeforeEach
    public void initializeProducerFactory() {
        producerFactory = new ReplicationProducerFactory(
                mockedConfigsFactory,
                mockedKeySerializerFactory,
                mockedValueSerializerFactory
        );
    }

    @Test
    public void producerShouldBeCreated() {
        String givenTopicName = "test-topic";
        Producer givenConfig = mock(Producer.class);

        @SuppressWarnings("unchecked") Map<String, Object> givenConfigsByKeys = mock(Map.class);
        when(mockedConfigsFactory.create(same(givenConfig))).thenReturn(givenConfigsByKeys);

        @SuppressWarnings("unchecked") Serializer<Object> givenKeySerializer = mock(Serializer.class);
        when(mockedKeySerializerFactory.create(same(givenConfig))).thenReturn(givenKeySerializer);

        @SuppressWarnings("unchecked") Serializer<ProducedReplication<?>> givenValueSerializer = mock(Serializer.class);
        when(mockedValueSerializerFactory.create()).thenReturn(givenValueSerializer);

        KafkaReplicationProducer actual = producerFactory.create(givenTopicName, givenConfig);

        String actualTopicName = getTopicName(actual);
        assertSame(givenTopicName, actualTopicName);

        ProducerFactory<?, ?> actualProducerFactory = getKafkaTemplate(actual).getProducerFactory();

        Serializer<?> actualKeySerializer = actualProducerFactory.getKeySerializer();
        assertSame(givenKeySerializer, actualKeySerializer);

        Serializer<?> actualValueSerializer = actualProducerFactory.getValueSerializer();
        assertSame(givenValueSerializer, actualValueSerializer);
    }

    private String getTopicName(KafkaReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_TOPIC_NAME, String.class);
    }

    private KafkaTemplate<?, ?> getKafkaTemplate(KafkaReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_KAFKA_TEMPLATE, KafkaTemplate.class);
    }
}
