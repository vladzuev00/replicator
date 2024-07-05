package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsConfig;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public final class ReplicationStreamsConfigFactoryTest {
    private static final String GIVEN_BOOTSTRAP_ADDRESS = "127.0.0.1:9092";

    private final ReplicationStreamsConfigFactory factory = new ReplicationStreamsConfigFactory(GIVEN_BOOTSTRAP_ADDRESS);

    @Test
    public void configShouldBeCreated() {
        String givenTopic = "test-topic";
        ReplicationConsumePipeline<?, ?> givenPipeline = createPipeline(givenTopic);

        StreamsConfig actual = factory.create(givenPipeline);
        StreamsConfig expected = new StreamsConfig(
                Map.of(
                        APPLICATION_ID_CONFIG, givenTopic,
                        BOOTSTRAP_SERVERS_CONFIG, GIVEN_BOOTSTRAP_ADDRESS,
                        PROCESSING_GUARANTEE_CONFIG, EXACTLY_ONCE_V2
                )
        );
        assertEquals(expected, actual);
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private ReplicationConsumePipeline<?, ?> createPipeline(String topic) {
        return ReplicationConsumePipeline.builder()
                .topic(topic)
                .idSerde(mock(Serde.class))
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(mock(JpaRepository.class))
                .build();
    }
}
