package by.aurorasoft.replicator.consumer;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.consumer.KafkaReplicationConsumer.ReplicationConsumingException;
import by.aurorasoft.replicator.model.TransportableReplication;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static by.aurorasoft.replicator.model.ReplicationType.SAVE;
import static java.lang.Integer.MIN_VALUE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaReplicationConsumerTest {
    private static final String GIVEN_TOPIC = "sync-dto";

    @Mock
    private AbsServiceCRUD<Long, ?, TestDto, ?> mockedService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private KafkaReplicationConsumer<Long, TestDto> consumer;

    @Captor
    private ArgumentCaptor<TestDto> dtoArgumentCaptor;

    @Before
    public void initializeConsumer() {
        consumer = new KafkaReplicationConsumer<>(
                mockedService,
                objectMapper,
                TestDto.class,
                null,
                GIVEN_TOPIC,
                null
        );
    }

    @Test
    public void recordsShouldBeListened() {
        final TransportableReplication givenReplication = new TransportableReplication(SAVE, "{\"id\":255}");
        final ConsumerRecord<Long, TransportableReplication> givenRecord = createRecord(255L, givenReplication);

        consumer.listen(givenRecord);

        verify(mockedService, times(1)).save(dtoArgumentCaptor.capture());

        final TestDto actualDto = dtoArgumentCaptor.getValue();
        final TestDto expectedDto = new TestDto(255L);
        assertEquals(expectedDto, actualDto);
    }

    @Test(expected = ReplicationConsumingException.class)
    public void recordsShouldNotBeListenedBecauseOfDtoDeserializationFailed() {
        final TransportableReplication givenReplication = new TransportableReplication(SAVE, "{\"id\":256");
        final ConsumerRecord<Long, TransportableReplication> givenRecord = createRecord(256L, givenReplication);

        consumer.listen(givenRecord);
    }

    private static ConsumerRecord<Long, TransportableReplication> createRecord(final Long id,
                                                                               final TransportableReplication replication) {
        return new ConsumerRecord<>(GIVEN_TOPIC, MIN_VALUE, MIN_VALUE, id, replication);
    }
}
