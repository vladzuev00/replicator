package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.Replication;
import by.aurorasoft.replicator.model.SaveReplication;
import by.aurorasoft.replicator.model.UpdateReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import static by.aurorasoft.replicator.model.ReplicationType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaReplicationProducerTest {
    private static final String GIVEN_TOPIC_NAME = "sync-dto";

    @Mock
    private KafkaTemplate<Long, TransportableReplication> mockedKafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private KafkaReplicationProducer<Long, TestDto> producer;

    @Captor
    private ArgumentCaptor<ProducerRecord<Long, TransportableReplication>> recordArgumentCaptor;

    @Before
    public void initializeProducer() {
        producer = new KafkaReplicationProducer<>(GIVEN_TOPIC_NAME, mockedKafkaTemplate, objectMapper);
    }

    @Test
    public void replicationShouldBeSent() {
        final Long givenId = 255L;
        final TestDto givenDto = new TestDto(givenId);
        final Replication<Long, TestDto> givenReplication = new SaveReplication<>(givenDto);

        producer.send(givenReplication);

        verify(mockedKafkaTemplate).send(recordArgumentCaptor.capture());

        final ProducerRecord<Long, TransportableReplication> actualRecord = recordArgumentCaptor.getValue();
        final TransportableReplication expectedReplication = new TransportableReplication(SAVE, "{\"id\":255}");
        final ProducerRecord<Long, TransportableReplication> expectedRecord = new ProducerRecord<>(
                GIVEN_TOPIC_NAME, givenId, expectedReplication
        );
        assertEquals(expectedRecord, actualRecord);
    }

    @Test
    public void modelShouldBeConvertedToTransportable() {
        final Long givenId = 256L;
        final TestDto givenDto = new TestDto(givenId);
        final Replication<Long, TestDto> givenReplication = new UpdateReplication<>(givenDto);

        final TransportableReplication actual = producer.convertModelToTransportable(givenReplication);
        final TransportableReplication expected = new TransportableReplication(UPDATE, "{\"id\":256}");
        assertEquals(expected, actual);
    }

    @Test
    public void transportableShouldBeConvertedToTopicValue() {
        final TransportableReplication givenReplication = new TransportableReplication(DELETE, "{\"id\":256}");

        final TransportableReplication actual = producer.convertTransportableToTopicValue(givenReplication);
        assertSame(givenReplication, actual);
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
