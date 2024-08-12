package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting.EntityViewSetting;
import by.aurorasoft.replicator.testentity.TestEntity;
import by.aurorasoft.replicator.transaction.manager.ReplicationTransactionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerTest {
    private static final String GIVEN_TOPIC = "test-topic";
    private static final EntityViewSetting[] GIVEN_ENTITY_VIEW_SETTINGS = new EntityViewSetting[]{
            new EntityViewSetting(TestEntity.class, new String[]{"secondProperty"})
    };

    @Mock
    private SaveProducedReplicationFactory mockedSaveReplicationFactory;

    @Mock
    private KafkaTemplate<Object, ProducedReplication<?>> mockedKafkaTemplate;

    @Mock
    private ReplicationTransactionManager mockedTransactionManager;

    private ReplicationProducer producer;

    @BeforeEach
    public void initializeProducer() {
        producer = new ReplicationProducer(
                mockedSaveReplicationFactory,
                mockedKafkaTemplate,
                mockedTransactionManager,
                GIVEN_TOPIC,
                GIVEN_ENTITY_VIEW_SETTINGS
        );
    }

    @Test
    public void saveShouldBeProducedAfterCommit() {

    }
}
