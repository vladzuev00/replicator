package by.aurorasoft.replicator.transactioncallback;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ProduceReplicationTransactionCallbackTest {
    private static final String GIVEN_TOPIC = "test-topic";

    @Mock
    private ProducedReplication<?> mockedReplication;

    @Mock
    private KafkaTemplate<Object, ProducedReplication<?>> mockedKafkaTemplate;

    private ProduceReplicationTransactionCallback callback;

    @Captor
    private ArgumentCaptor<ProducerRecord<Object, ProducedReplication<?>>> recordCaptor;

    @BeforeEach
    public void initializeCallback() {
        callback = new ProduceReplicationTransactionCallback(GIVEN_TOPIC, mockedReplication, mockedKafkaTemplate);
    }

    @Test
    public void callbackShouldBeCalled() {
        Object givenDtoId = new Object();
        when(mockedReplication.getDtoId()).thenReturn(givenDtoId);

        callback.afterCommit();

        verify(mockedKafkaTemplate, times(1)).send(recordCaptor.capture());
        ProducerRecord<Object, ProducedReplication<?>> actual = recordCaptor.getValue();
        var expected = new ProducerRecord<>(GIVEN_TOPIC, givenDtoId, mockedReplication);
        assertEquals(expected, actual);
    }
}
