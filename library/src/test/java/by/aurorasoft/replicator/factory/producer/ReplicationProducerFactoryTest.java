package by.aurorasoft.replicator.factory.producer;

import by.aurorasoft.replicator.factory.kafkatemplate.ReplicationKafkaTemplateFactory;
import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting.EntityViewSetting;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.testentity.TestEntity;
import by.aurorasoft.replicator.testrepository.TestRepository;
import by.aurorasoft.replicator.transaction.manager.ReplicationTransactionManager;
import org.apache.kafka.common.serialization.LongSerializer;
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
    private static final String FIELD_NAME_SAVE_REPLICATION_FACTORY = "saveReplicationFactory";
    private static final String FIELD_NAME_KAFKA_TEMPLATE = "kafkaTemplate";
    private static final String FIELD_NAME_TRANSACTION_MANAGER = "transactionManager";
    private static final String FIELD_NAME_TOPIC = "topic";
    private static final String FIELD_NAME_ENTITY_VIEW_SETTINGS = "entityViewSettings";

    @Mock
    private ReplicationKafkaTemplateFactory mockedKafkaTemplateFactory;

    @Mock
    private SaveProducedReplicationFactory mockedSaveReplicationFactory;

    @Mock
    private ReplicationTransactionManager mockedTransactionManager;

    private ReplicationProducerFactory producerFactory;

    @BeforeEach
    public void initializeProducerFactory() {
        producerFactory = new ReplicationProducerFactory(
                mockedKafkaTemplateFactory,
                mockedSaveReplicationFactory,
                mockedTransactionManager
        );
    }

    @Test
    public void producerShouldBeCreated() {
        String givenTopic = "test-topic";
        EntityViewSetting[] givenEntityViewSettings = {
                new EntityViewSetting(Object.class, new String[]{"firstProperty", "secondProperty"}),
                new EntityViewSetting(Integer.class, new String[]{"value"}),
        };
        ReplicationProduceSetting<TestEntity, Long> givenSetting = ReplicationProduceSetting.<TestEntity, Long>builder()
                .topic(givenTopic)
                .repository(new TestRepository())
                .idSerializer(new LongSerializer())
                .entityViewSettings(givenEntityViewSettings)
                .build();

        @SuppressWarnings("unchecked") KafkaTemplate<Object, ProducedReplication<?>> givenKafkaTemplate = mock(
                KafkaTemplate.class
        );
        when(mockedKafkaTemplateFactory.create(same(givenSetting))).thenReturn(givenKafkaTemplate);

        ReplicationProducer actual = producerFactory.create(givenSetting);

        SaveProducedReplicationFactory actualSaveReplicationFactory = getSaveReplicationFactory(actual);
        assertSame(mockedSaveReplicationFactory, actualSaveReplicationFactory);

        KafkaTemplate<?, ?> actualKafkaTemplate = getKafkaTemplate(actual);
        assertSame(givenKafkaTemplate, actualKafkaTemplate);

        ReplicationTransactionManager actualTransactionManager = getTransactionManager(actual);
        assertSame(mockedTransactionManager, actualTransactionManager);

        String actualTopic = getTopic(actual);
        assertSame(givenTopic, actualTopic);

        EntityViewSetting[] actualEntityViewSettings = getEntityViewSettings(actual);
        assertSame(givenEntityViewSettings, actualEntityViewSettings);
    }

    private SaveProducedReplicationFactory getSaveReplicationFactory(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_SAVE_REPLICATION_FACTORY, SaveProducedReplicationFactory.class);
    }

    private KafkaTemplate<?, ?> getKafkaTemplate(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_KAFKA_TEMPLATE, KafkaTemplate.class);
    }

    private ReplicationTransactionManager getTransactionManager(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_TRANSACTION_MANAGER, ReplicationTransactionManager.class);
    }

    private String getTopic(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_TOPIC, String.class);
    }

    private EntityViewSetting[] getEntityViewSettings(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_ENTITY_VIEW_SETTINGS, EntityViewSetting[].class);
    }
}
