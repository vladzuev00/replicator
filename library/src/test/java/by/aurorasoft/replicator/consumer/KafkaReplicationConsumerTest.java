package by.aurorasoft.replicator.consumer;

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
public final class KafkaReplicationConsumerTest {
    private static final String GIVEN_TOPIC = "sync-dto";

    @Mock
    private AbsServiceCRUD<Long, ?, TestDto, ?> mockedService;

    private KafkaReplicationConsumer<Long, TestDto> consumer;

    @Before
    public void initializeConsumer() {
        consumer = new KafkaReplicationConsumer<>(mockedService, null, GIVEN_TOPIC, null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void recordsShouldBeListened() {
        final Replication<Long, TestDto> givenReplication = mock(Replication.class);
        final ConsumerRecord<Long, Replication<Long, TestDto>> givenRecord = createRecord(255L, givenReplication);

        consumer.listen(givenRecord);

        verify(givenReplication, times(1)).execute(same(mockedService));
    }

    @SuppressWarnings("SameParameterValue")
    private static ConsumerRecord<Long, Replication<Long, TestDto>> createRecord(final Long id,
                                                                                 final Replication<Long, TestDto> replication) {
        return new ConsumerRecord<>(GIVEN_TOPIC, MIN_VALUE, MIN_VALUE, id, replication);
    }
}
