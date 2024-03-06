package by.aurorasoft.replicator.consuming.consumer;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.lang.Integer.MIN_VALUE;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationConsumerTest {
    private static final String GIVEN_TOPIC = "sync-dto";

    @Mock
    private ReplicationConsumerConfig<Long, TestDto> mockedConfig;

    private ReplicationConsumer<Long, TestDto> consumer;

    @Before
    public void initializeConsumer() {
        consumer = new ReplicationConsumer<>(mockedConfig);
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void recordsShouldBeListened() {
        final Replication<Long, TestDto> givenReplication = mock(Replication.class);
        final ConsumerRecord<Long, Replication<Long, TestDto>> givenRecord = createRecord(255L, givenReplication);

        final AbsServiceCRUD givenService = mock(AbsServiceCRUD.class);
        when(mockedConfig.getService()).thenReturn(givenService);

        consumer.listen(givenRecord);

        verify(givenReplication, times(1)).execute(givenService);
    }

    @SuppressWarnings("SameParameterValue")
    private static ConsumerRecord<Long, Replication<Long, TestDto>> createRecord(final Long id,
                                                                                 final Replication<Long, TestDto> replication) {
        return new ConsumerRecord<>(GIVEN_TOPIC, MIN_VALUE, MIN_VALUE, id, replication);
    }
}
