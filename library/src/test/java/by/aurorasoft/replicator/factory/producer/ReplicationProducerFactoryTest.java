package by.aurorasoft.replicator.factory.producer;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.factory.kafkatemplate.ReplicationKafkaTemplateFactory;
import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static by.aurorasoft.replicator.testutil.ReplicatedServiceUtil.createReplicatedService;
import static by.aurorasoft.replicator.testutil.TopicConfigUtil.createTopicConfig;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerFactoryTest {
    private static final String FIELD_NAME_PRODUCER_SAVE_REPLICATION_FACTORY = "saveReplicationFactory";
    private static final String FIELD_NAME_PRODUCER_KAFKA_TEMPLATE = "kafkaTemplate";
    private static final String FIELD_NAME_PRODUCER_TOPIC = "topic";
    private static final String FIELD_NAME_PRODUCER_DTO_VIEW_CONFIGS = "dtoViewConfigs";

    @Mock
    private ReplicationKafkaTemplateFactory mockedKafkaTemplateFactory;

    @Mock
    private SaveProducedReplicationFactory mockedSaveReplicationFactory;

    private ReplicationProducerFactory producerFactory;

    @BeforeEach
    public void initializeProducerFactory() {
        producerFactory = new ReplicationProducerFactory(mockedKafkaTemplateFactory, mockedSaveReplicationFactory);
    }

    @Test
    public void producerShouldBeCreated() {
        ProducerConfig givenProducerConfig = mock(ProducerConfig.class);
        String givenTopicName = "test-topic";
        TopicConfig givenTopicConfig = createTopicConfig(givenTopicName);
        DtoViewConfig[] givenDtoViewConfigs = new DtoViewConfig[]{mock(DtoViewConfig.class), mock(DtoViewConfig.class)};
        var givenService = createReplicatedService(givenProducerConfig, givenTopicConfig, givenDtoViewConfigs);

        @SuppressWarnings("unchecked") KafkaTemplate<Object, ProducedReplication<?>> givenKafkaTemplate = mock(
                KafkaTemplate.class
        );
        when(mockedKafkaTemplateFactory.create(same(givenProducerConfig))).thenReturn(givenKafkaTemplate);

        ReplicationProducer actual = producerFactory.create(givenService);

        SaveProducedReplicationFactory actualSaveReplicationFactory = getSaveReplicationFactory(actual);
        assertSame(mockedSaveReplicationFactory, actualSaveReplicationFactory);

        KafkaTemplate<?, ?> actualKafkaTemplate = getKafkaTemplate(actual);
        assertSame(givenKafkaTemplate, actualKafkaTemplate);

        String actualTopicName = getTopicName(actual);
        assertSame(givenTopicName, actualTopicName);

        DtoViewConfig[] actualDtoViewConfigs = getDtoViewConfigs(actual);
        assertSame(givenDtoViewConfigs, actualDtoViewConfigs);
    }

    private SaveProducedReplicationFactory getSaveReplicationFactory(ReplicationProducer producer) {
        return getFieldValue(
                producer,
                FIELD_NAME_PRODUCER_SAVE_REPLICATION_FACTORY,
                SaveProducedReplicationFactory.class
        );
    }

    private KafkaTemplate<?, ?> getKafkaTemplate(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_PRODUCER_KAFKA_TEMPLATE, KafkaTemplate.class);
    }

    private String getTopicName(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_PRODUCER_TOPIC, String.class);
    }

    private DtoViewConfig[] getDtoViewConfigs(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_PRODUCER_DTO_VIEW_CONFIGS, DtoViewConfig[].class);
    }
}
