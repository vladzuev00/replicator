package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.model.Replication;
import by.aurorasoft.replicator.model.SaveReplication;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationProducerTest {
    private static final String GIVEN_TOPIC_NAME = "sync-dto";

    @Mock
    private KafkaTemplate<Long, Replication<Long, TestDto>> mockedKafkaTemplate;

    private ReplicationProducer<Long, TestDto> producer;

    @Captor
    private ArgumentCaptor<ProducerRecord<Long, Replication<Long, TestDto>>> recordArgumentCaptor;

    @Before
    public void initializeProducer() {
        producer = new ReplicationProducer<>(GIVEN_TOPIC_NAME, mockedKafkaTemplate);
    }

    @Test
    public void replicationShouldBeSent() {
        final Long givenId = 255L;
        final TestDto givenDto = new TestDto(givenId);
        final Replication<Long, TestDto> givenReplication = new SaveReplication<>(givenDto);

        producer.send(givenReplication);

        verify(mockedKafkaTemplate).send(recordArgumentCaptor.capture());

        final ProducerRecord<Long, Replication<Long, TestDto>> actualSentRecord = recordArgumentCaptor.getValue();
        final ProducerRecord<Long, Replication<Long, TestDto>> expectedSentRecord = new ProducerRecord<>(
                GIVEN_TOPIC_NAME,
                givenId,
                givenReplication
        );
        assertEquals(expectedSentRecord, actualSentRecord);
    }
}