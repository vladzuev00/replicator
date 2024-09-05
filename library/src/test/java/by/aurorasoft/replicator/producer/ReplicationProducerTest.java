package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.testcrud.TestDto;
import by.aurorasoft.replicator.transaction.callback.ProduceReplicationTransactionCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerTest {
    private static final String GIVEN_TOPIC = "test-topic";
    private static final DtoViewConfig[] GIVEN_DTO_VIEW_CONFIGS = new DtoViewConfig[]{
            mock(DtoViewConfig.class),
            mock(DtoViewConfig.class),
            mock(DtoViewConfig.class)
    };

    private static final String FIELD_NAME_CALLBACK_KAFKA_TEMPLATE = "kafkaTemplate";
    private static final String FIELD_NAME_CALLBACK_REPLICATION = "replication";
    private static final String FIELD_NAME_CALLBACK_TOPIC = "topic";

    @Mock
    private SaveProducedReplicationFactory mockedSaveReplicationFactory;

    @Mock
    private KafkaTemplate<Object, ProducedReplication<?>> mockedKafkaTemplate;

    private ReplicationProducer producer;

    @Captor
    private ArgumentCaptor<ProduceReplicationTransactionCallback> callbackCaptor;

    @BeforeEach
    public void initializeProducer() {
        producer = new ReplicationProducer(
                mockedSaveReplicationFactory,
                mockedKafkaTemplate,
                GIVEN_TOPIC,
                GIVEN_DTO_VIEW_CONFIGS
        );
    }

    @Test
    public void saveShouldBeProducedAfterCommit() {
        try (var mockedTransactionSynchronizationManager = mockStatic(TransactionSynchronizationManager.class)) {
            TestDto givenSavedDto = TestDto.builder().build();

            SaveProducedReplication givenReplication = mock(SaveProducedReplication.class);
            when(mockedSaveReplicationFactory.create(same(givenSavedDto), same(GIVEN_DTO_VIEW_CONFIGS)))
                    .thenReturn(givenReplication);

            producer.produceSaveAfterCommit(givenSavedDto);

            mockedTransactionSynchronizationManager
                    .verify(() -> registerSynchronization(callbackCaptor.capture()), times(1));
            ProduceReplicationTransactionCallback capturedCallback = callbackCaptor.getValue();

            KafkaTemplate<?, ?> actualKafkaTemplate = getKafkaTemplate(capturedCallback);
            assertSame(mockedKafkaTemplate, actualKafkaTemplate);

            ProducedReplication<?> actualReplication = getReplication(capturedCallback);
            assertSame(givenReplication, actualReplication);

            String actualTopic = getTopic(capturedCallback);
            assertSame(GIVEN_TOPIC, actualTopic);
        }
    }

    @Test
    public void deleteShouldBeProducedAfterCommit() {
        try (var mockedTransactionSynchronizationManager = mockStatic(TransactionSynchronizationManager.class)) {
            Long givenDtoId = 255L;

            producer.produceDeleteAfterCommit(givenDtoId);

            mockedTransactionSynchronizationManager
                    .verify(() -> registerSynchronization(callbackCaptor.capture()), times(1));
            ProduceReplicationTransactionCallback capturedCallback = callbackCaptor.getValue();

            KafkaTemplate<?, ?> actualKafkaTemplate = getKafkaTemplate(capturedCallback);
            assertSame(mockedKafkaTemplate, actualKafkaTemplate);

            ProducedReplication<?> actualReplication = getReplication(capturedCallback);
            ProducedReplication<?> expectedReplication = new DeleteProducedReplication(givenDtoId);
            assertEquals(expectedReplication, actualReplication);

            String actualTopic = getTopic(capturedCallback);
            assertSame(GIVEN_TOPIC, actualTopic);
        }
    }

    private KafkaTemplate<?, ?> getKafkaTemplate(ProduceReplicationTransactionCallback callback) {
        return getFieldValue(callback, FIELD_NAME_CALLBACK_KAFKA_TEMPLATE, KafkaTemplate.class);
    }

    private ProducedReplication<?> getReplication(ProduceReplicationTransactionCallback callback) {
        return getFieldValue(callback, FIELD_NAME_CALLBACK_REPLICATION, ProducedReplication.class);
    }

    private String getTopic(ProduceReplicationTransactionCallback callback) {
        return getFieldValue(callback, FIELD_NAME_CALLBACK_TOPIC, String.class);
    }
}
