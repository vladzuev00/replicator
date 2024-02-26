package by.aurorasoft.replicator.consumer;

import by.aurorasoft.replicator.model.ReplicationType;
import by.aurorasoft.replicator.model.TransportableReplication.Fields;
import by.aurorasoft.replicator.model.replication.Replication;
import by.aurorasoft.replicator.model.replication.SaveReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static by.aurorasoft.replicator.model.ReplicationType.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaReplicationConsumerTest {
    private static final String GIVEN_TOPIC = "sync-dto";

    @Mock
    private AbsServiceCRUD<Long, ?, TestDto, ?> mockedService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private KafkaReplicationConsumer<Long, TestDto> consumer;

    @Captor
    private ArgumentCaptor<TestDto> dtoArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Before
    public void initializeConsumer() {
        consumer = new TestKafkaReplicationConsumer(mockedService, objectMapper);
    }

    @Test
    public void recordShouldBeMappedToReplication() {
        final String givenDtoJson = "{\"id\":255}";
        final GenericRecord givenGenericRecord = createGenericRecord(SAVE, givenDtoJson);

        final Replication<Long, TestDto> actual = consumer.map(givenGenericRecord);
        final Replication<Long, TestDto> expected = new SaveReplication<>(new TestDto(255L));
        assertEquals(expected, actual);
    }

    @Test
    public void recordsShouldBeListened() {
        final List<ConsumerRecord<Long, GenericRecord>> givenConsumerRecords = List.of(
                createConsumerRecord(255L, SAVE, "{\"id\":255}"),
                createConsumerRecord(256L, UPDATE, "{\"id\":256}"),
                createConsumerRecord(257L, DELETE, "{\"id\":257}")
        );

        consumer.listen(givenConsumerRecords);

        verify(mockedService, times(1)).save(dtoArgumentCaptor.capture());
        verify(mockedService, times(1)).update(dtoArgumentCaptor.capture());
        verify(mockedService, times(1)).delete(longArgumentCaptor.capture());

        final List<TestDto> actualCapturedDtos = dtoArgumentCaptor.getAllValues();

        final TestDto actualSavedDto = actualCapturedDtos.get(0);
        final TestDto expectedSavedDto = new TestDto(255L);
        assertEquals(expectedSavedDto, actualSavedDto);

        final TestDto actualUpdatedDto = actualCapturedDtos.get(1);
        final TestDto expectedUpdatedDto = new TestDto(256L);
        assertEquals(expectedUpdatedDto, actualUpdatedDto);

        final Long actualDeletedEntityId = longArgumentCaptor.getValue();
        final Long expectedDeletedEntityId = 257L;
        assertEquals(expectedDeletedEntityId, actualDeletedEntityId);
    }

    private static ConsumerRecord<Long, GenericRecord> createConsumerRecord(final Long id,
                                                                            final ReplicationType replicationType,
                                                                            final String dtoJson) {
        final GenericRecord genericRecord = createGenericRecord(replicationType, dtoJson);
        return new ConsumerRecord<>(GIVEN_TOPIC, Integer.MIN_VALUE, Long.MIN_VALUE, id, genericRecord);
    }

    @SuppressWarnings("SameParameterValue")
    private static GenericRecord createGenericRecord(final ReplicationType replicationType, final String dtoJson) {
        final GenericRecord record = mock(GenericRecord.class);
        when(record.get(same(Fields.type))).thenReturn(replicationType);
        when(record.get(same(Fields.dtoJson))).thenReturn(dtoJson);
        return record;
    }

    private static final class TestKafkaReplicationConsumer extends KafkaReplicationConsumer<Long, TestDto> {

        public TestKafkaReplicationConsumer(final AbsServiceCRUD<Long, ?, TestDto, ?> service,
                                            final ObjectMapper objectMapper) {
            super(service, objectMapper, TestDto.class);
        }
    }

    @Value
    private static class TestDto implements AbstractDto<Long> {
        Long id;

        @JsonCreator
        public TestDto(@JsonProperty("id") final Long id) {
            this.id = id;
        }
    }
}
