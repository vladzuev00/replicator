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
        final String givenPipelineId = "test-pipeline";
        final ReplicationConsumePipeline<?, ?> givenPipeline = createPipeline(givenPipelineId);

        final StreamsConfig actual = factory.create(givenPipeline);
        final StreamsConfig expected = createExpected(givenPipelineId);
        assertEquals(expected, actual);
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    private static ReplicationConsumePipeline<?, ?> createPipeline(final String id) {
        return ReplicationConsumePipeline.builder()
                .id(id)
                .topic("test-topic")
                .idSerde(mock(Serde.class))
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(mock(JpaRepository.class))
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static StreamsConfig createExpected(final String pipelineId) {
        final Map<String, Object> configsByNames = Map.of(
                APPLICATION_ID_CONFIG, pipelineId,
                BOOTSTRAP_SERVERS_CONFIG, GIVEN_BOOTSTRAP_ADDRESS,
                PROCESSING_GUARANTEE_CONFIG, EXACTLY_ONCE_V2
        );
        return new StreamsConfig(configsByNames);
    }
}
