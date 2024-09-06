package by.aurorasoft.replicator.factory.producer;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.factory.kafkatemplate.ReplicationKafkaTemplateFactory;
import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.testcrud.TestDto;
import by.aurorasoft.replicator.testcrud.TestService;
import by.aurorasoft.replicator.testutil.DtoViewConfigUtil;
import by.aurorasoft.replicator.testutil.ProducerConfigUtil;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static by.aurorasoft.replicator.testutil.DtoViewConfigUtil.createDtoViewConfig;
import static by.aurorasoft.replicator.testutil.ProducerConfigUtil.createProducerConfig;
import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
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

    @Captor
    private ArgumentCaptor<ProducerConfig> producerConfigCaptor;

    @BeforeEach
    public void initializeProducerFactory() {
        producerFactory = new ReplicationProducerFactory(mockedKafkaTemplateFactory, mockedSaveReplicationFactory);
    }

    @Test
    public void producerShouldBeCreated() {
        TestService givenService = new TestService(null);

        @SuppressWarnings("unchecked") KafkaTemplate<Object, ProducedReplication<?>> givenKafkaTemplate = mock(
                KafkaTemplate.class
        );
        when(mockedKafkaTemplateFactory.create(any(ProducerConfig.class))).thenReturn(givenKafkaTemplate);

        ReplicationProducer actual = producerFactory.create(givenService);

        SaveProducedReplicationFactory actualSaveReplicationFactory = getSaveReplicationFactory(actual);
        assertSame(mockedSaveReplicationFactory, actualSaveReplicationFactory);

        KafkaTemplate<?, ?> actualKafkaTemplate = getKafkaTemplate(actual);
        assertSame(givenKafkaTemplate, actualKafkaTemplate);

        String actualTopic = getTopic(actual);
        String expectedTopic = "sync-dto";
        Assertions.assertEquals(expectedTopic, actualTopic);

        DtoViewConfig[] actualDtoViewConfigs = getDtoViewConfigs(actual);
        DtoViewConfig[] expectedDtoViewConfigs = {
                createDtoViewConfig(TestDto.class, new String[]{"firstProperty"}, new String[]{"secondProperty"})
        };
        DtoViewConfigUtil.assertEquals(expectedDtoViewConfigs, actualDtoViewConfigs);

        verify(mockedKafkaTemplateFactory, times(1)).create(producerConfigCaptor.capture());
        ProducerConfig actualProducerConfig = producerConfigCaptor.getValue();
        ProducerConfig expectedProducerConfig = createProducerConfig(LongSerializer.class, 11, 501, 100001);
        ProducerConfigUtil.assertEquals(expectedProducerConfig, actualProducerConfig);
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

    private String getTopic(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_PRODUCER_TOPIC, String.class);
    }

    private DtoViewConfig[] getDtoViewConfigs(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_PRODUCER_DTO_VIEW_CONFIGS, DtoViewConfig[].class);
    }
}
