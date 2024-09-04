//package by.aurorasoft.replicator.producer;
//
//import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
//import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
//import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
//import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
//import by.aurorasoft.replicator.testcrud.TestEntity;
//import by.aurorasoft.replicator.transaction.callback.ProduceReplicationTransactionCallback;
//import by.aurorasoft.replicator.transaction.manager.ReplicationTransactionManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public final class ReplicationProducerTest {
//    private static final String GIVEN_TOPIC = "test-topic";
//    private static final EntityViewSetting[] GIVEN_ENTITY_VIEW_SETTINGS = new EntityViewSetting[]{
//            new EntityViewSetting(TestEntity.class, new String[]{"secondProperty"})
//    };
//
//    private static final String FIELD_NAME_CALLBACK_KAFKA_TEMPLATE = "kafkaTemplate";
//    private static final String FIELD_NAME_CALLBACK_REPLICATION = "replication";
//    private static final String FIELD_NAME_CALLBACK_TOPIC = "topic";
//
//    @Mock
//    private SaveProducedReplicationFactory mockedSaveReplicationFactory;
//
//    @Mock
//    private KafkaTemplate<Object, ProducedReplication<?>> mockedKafkaTemplate;
//
//    @Mock
//    private ReplicationTransactionManager mockedTransactionManager;
//
//    private ReplicationProducer producer;
//
//    @Captor
//    private ArgumentCaptor<ProduceReplicationTransactionCallback> callbackCaptor;
//
//    @BeforeEach
//    public void initializeProducer() {
//        producer = new ReplicationProducer(
//                mockedSaveReplicationFactory,
//                mockedKafkaTemplate,
//                mockedTransactionManager,
//                GIVEN_TOPIC,
//                GIVEN_ENTITY_VIEW_SETTINGS
//        );
//    }
//
//    @Test
//    public void saveShouldBeProducedAfterCommit() {
//        TestEntity givenSavedEntity = new TestEntity();
//
//        SaveProducedReplication givenReplication = mock(SaveProducedReplication.class);
//        when(mockedSaveReplicationFactory.create(same(givenSavedEntity), same(GIVEN_ENTITY_VIEW_SETTINGS)))
//                .thenReturn(givenReplication);
//
//        producer.produceSaveAfterCommit(givenSavedEntity);
//
//        verify(mockedTransactionManager, times(1)).callAfterCommit(callbackCaptor.capture());
//        ProduceReplicationTransactionCallback capturedCallback = callbackCaptor.getValue();
//
//        KafkaTemplate<Object, ProducedReplication<?>> actualKafkaTemplate = getKafkaTemplate(capturedCallback);
//        assertSame(mockedKafkaTemplate, actualKafkaTemplate);
//
//        ProducedReplication<?> actualReplication = getReplication(capturedCallback);
//        assertSame(givenReplication, actualReplication);
//
//        String actualTopic = getTopic(capturedCallback);
//        assertSame(GIVEN_TOPIC, actualTopic);
//    }
//
//    @Test
//    public void deleteShouldBeProducedAfterCommit() {
//        Long givenEntityId = 255L;
//
//        producer.produceDeleteAfterCommit(givenEntityId);
//
//        verify(mockedTransactionManager, times(1)).callAfterCommit(callbackCaptor.capture());
//        ProduceReplicationTransactionCallback capturedCallback = callbackCaptor.getValue();
//
//        KafkaTemplate<Object, ProducedReplication<?>> actualKafkaTemplate = getKafkaTemplate(capturedCallback);
//        assertSame(mockedKafkaTemplate, actualKafkaTemplate);
//
//        ProducedReplication<?> actualReplication = getReplication(capturedCallback);
//        ProducedReplication<?> expectedReplication = new DeleteProducedReplication(givenEntityId);
//        assertEquals(expectedReplication, actualReplication);
//
//        String actualTopic = getTopic(capturedCallback);
//        assertSame(GIVEN_TOPIC, actualTopic);
//    }
//
//    @SuppressWarnings("unchecked")
//    private KafkaTemplate<Object, ProducedReplication<?>> getKafkaTemplate(ProduceReplicationTransactionCallback callback) {
//        return getFieldValue(callback, FIELD_NAME_CALLBACK_KAFKA_TEMPLATE, KafkaTemplate.class);
//    }
//
//    private ProducedReplication<?> getReplication(ProduceReplicationTransactionCallback callback) {
//        return getFieldValue(callback, FIELD_NAME_CALLBACK_REPLICATION, ProducedReplication.class);
//    }
//
//    private String getTopic(ProduceReplicationTransactionCallback callback) {
//        return getFieldValue(callback, FIELD_NAME_CALLBACK_TOPIC, String.class);
//    }
//}
