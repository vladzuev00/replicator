package by.aurorasoft.replicator.consuming.consumer;

import by.aurorasoft.replicator.base.entity.TestEntity;
import by.aurorasoft.replicator.event.ReplicationEvent;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;

import static java.lang.Integer.MIN_VALUE;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationConsumerTest {
    private static final String GIVEN_TOPIC = "sync-dto";

    @Mock
    private JpaRepository<TestEntity, Long> mockedRepository;

    @Mock
    private ApplicationEventPublisher mockedEventPublisher;

    private ReplicationConsumer<Long, TestEntity> consumer;

    @Captor
    private ArgumentCaptor<ReplicationEvent<Long, TestEntity>> eventArgumentCaptor;

    @Before
    public void initializeConsumer() {
        consumer = new ReplicationConsumer<>(
                null,
                GIVEN_TOPIC,
                null,
                null,
                mockedRepository,
                mockedEventPublisher
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void recordShouldBeListened() {
        final ConsumedReplication<Long, TestEntity> givenReplication = mock(ConsumedReplication.class);
        final ConsumerRecord<Long, ConsumedReplication<Long, TestEntity>> givenRecord = createRecord(
                255L,
                givenReplication
        );

        consumer.listen(givenRecord);

        verify(mockedEventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());

        final ReplicationEvent<Long, TestEntity> actualEvent = eventArgumentCaptor.getValue();

        final ReplicationConsumer<Long, TestEntity> actualEventSource = actualEvent.getSource();
        assertSame(consumer, actualEventSource);

        final ConsumedReplication<Long, TestEntity> actualReplication = actualEvent.getReplication();
        assertSame(givenReplication, actualReplication);
    }

    @SuppressWarnings("SameParameterValue")
    private static ConsumerRecord<Long, ConsumedReplication<Long, TestEntity>> createRecord(
            final Long id,
            final ConsumedReplication<Long, TestEntity> replication
    ) {
        return new ConsumerRecord<>(GIVEN_TOPIC, MIN_VALUE, MIN_VALUE, id, replication);
    }
}
