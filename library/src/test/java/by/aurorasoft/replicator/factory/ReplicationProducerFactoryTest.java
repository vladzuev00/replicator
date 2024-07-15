package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

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
    private ReplicationKafkaTemplateFactory mockedKafkaTemplateFactory;

    private ReplicationProducerFactory producerFactory;

    @BeforeEach
    public void initializeProducerFactory() {
        producerFactory = new ReplicationProducerFactory(mockedKafkaTemplateFactory);
    }

    @Test
    public void producerShouldBeCreated() {
        String givenTopicName = "sync-dto";
        ProducerConfig givenProducerConfig = mock(ProducerConfig.class);
        ReplicatedService givenReplicatedService = createReplicatedService(givenTopicName, givenProducerConfig);
        KafkaTemplate<Object, ProducedReplication<?>> givenKafkaTemplate = mockKafkaTemplateFor(givenProducerConfig);

        ReplicationProducer actual = producerFactory.create(givenReplicatedService);

        String actualTopicName = getTopicName(actual);
        assertSame(givenTopicName, actualTopicName);

        KafkaTemplate<?, ?> actualKafkaTemplate = getKafkaTemplate(actual);
        assertSame(givenKafkaTemplate, actualKafkaTemplate);
    }

    private ReplicatedService createReplicatedService(String topicName, ProducerConfig producerConfig) {
        TopicConfig topicConfig = createTopicConfig(topicName);
        ReplicatedService service = mock(ReplicatedService.class);
        when(service.topicConfig()).thenReturn(topicConfig);
        when(service.producerConfig()).thenReturn(producerConfig);
        return service;
    }

    private TopicConfig createTopicConfig(String name) {
        TopicConfig config = mock(TopicConfig.class);
        when(config.name()).thenReturn(name);
        return config;
    }

    @SuppressWarnings("unchecked")
    private KafkaTemplate<Object, ProducedReplication<?>> mockKafkaTemplateFor(ProducerConfig producerConfig) {
        KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate = mock(KafkaTemplate.class);
        when(mockedKafkaTemplateFactory.create(same(producerConfig))).thenReturn(kafkaTemplate);
        return kafkaTemplate;
    }

    private String getTopicName(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_TOPIC_NAME, String.class);
    }

    private KafkaTemplate<?, ?> getKafkaTemplate(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_KAFKA_TEMPLATE, KafkaTemplate.class);
    }
}
