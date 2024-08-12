package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting.EntityViewSetting;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import by.aurorasoft.replicator.producer.ReplicationProducer.TransactionCallback;
import by.aurorasoft.replicator.testentity.TestEntity;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static com.monitorjbl.json.Match.match;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerTest {
    private static final String GIVEN_TOPIC = "test-topic";
    private static final String EXCLUDED_FIELD_NAME = "secondProperty";
    private static final EntityViewSetting[] GIVEN_ENTITY_VIEW_SETTINGS = {
            new EntityViewSetting(TestEntity.class, new String[]{EXCLUDED_FIELD_NAME})
    };

    @Mock
    private KafkaTemplate<Object, ProducedReplication<?>> mockedKafkaTemplate;

    private ReplicationProducer producer;

    @Captor
    private ArgumentCaptor<TransactionCallback> transactionCallbackCaptor;

    @Captor
    private ArgumentCaptor<ProducerRecord<Object, ProducedReplication<?>>> recordCaptor;

    @BeforeEach
    public void initializeProducer() {
        producer = new ReplicationProducer(mockedKafkaTemplate, GIVEN_TOPIC, GIVEN_ENTITY_VIEW_SETTINGS);
    }

    @Test
    public void saveShouldBeProducedInCaseTransactionIsActive() {
        try (var mockedTransactionSynchronizationManager = mockStatic(TransactionSynchronizationManager.class)) {
            Long givenEntityId = 255L;
            TestEntity givenEntity = new TestEntity(givenEntityId, "first-value", "second-value");

            mockedTransactionSynchronizationManager
                    .when(TransactionSynchronizationManager::isActualTransactionActive)
                    .thenReturn(true);

            producer.produceSaveAfterCommit(givenEntity);

            EntityJsonView<TestEntity> expectedEntityJsonView = new EntityJsonView<>(givenEntity);
            expectedEntityJsonView.onClass(TestEntity.class, match().exclude(EXCLUDED_FIELD_NAME));
            SaveProducedReplication expectedReplication = new SaveProducedReplication(expectedEntityJsonView);



            verifyProducing(givenEntityId, expectedReplication);

            @SuppressWarnings("unchecked") var actualEntityJsonView = (EntityJsonView<TestEntity>) recordCaptor.getValue()
                    .value()
                    .getBody();
            TestEntity actualEntity = actualEntityJsonView.getEntity();
            assertSame(givenEntity, actualEntity);
        }
    }

//    @Test
//    public void saveShouldBeProducedInCaseTransactionIsActive() {
//        try (var mockedTransactionSynchronizationManager = mockStatic(TransactionSynchronizationManager.class)) {
//            Long givenEntityId = 255L;
//            TestEntity givenEntity = new TestEntity(givenEntityId, "first-value", "second-value");
//
//            mockedTransactionSynchronizationManager
//                    .when(TransactionSynchronizationManager::isActualTransactionActive)
//                    .thenReturn(true);
//
//            producer.produceSaveAfterCommit(givenEntity);
//
//            EntityJsonView<TestEntity> expectedEntityJsonView = new EntityJsonView<>(givenEntity);
//            expectedEntityJsonView.onClass(TestEntity.class, match().exclude(EXCLUDED_FIELD_NAME));
//            SaveProducedReplication expectedReplication = new SaveProducedReplication(expectedEntityJsonView);
//            verifyProducing(givenEntityId, expectedReplication);
//
//            @SuppressWarnings("unchecked") var actualEntityJsonView = (EntityJsonView<TestEntity>) recordCaptor.getValue()
//                    .value()
//                    .getBody();
//            TestEntity actualEntity = actualEntityJsonView.getEntity();
//            assertSame(givenEntity, actualEntity);
//        }
//    }

    @Test
    public void deleteShouldBeProduced() {
        Long givenEntityId = 255L;

        producer.produceDeleteAfterCommit(givenEntityId);

        verifyProducing(givenEntityId, new DeleteProducedReplication(givenEntityId));
    }

    private void verifyProducing(Object entityId, ProducedReplication<?> replication) {
        verify(mockedKafkaTemplate, times(1)).send(recordCaptor.capture());
        ProducerRecord<Object, ProducedReplication<?>> actual = recordCaptor.getValue();
        var expected = new ProducerRecord<>(GIVEN_TOPIC, entityId, replication);
        assertEquals(expected, actual);
    }
}
