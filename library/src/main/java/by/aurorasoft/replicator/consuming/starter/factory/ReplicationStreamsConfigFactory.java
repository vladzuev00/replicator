package by.aurorasoft.replicator.consuming.starter.factory;

import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;

@Component
public final class ReplicationStreamsConfigFactory {
    private final String bootstrapAddress;

    public ReplicationStreamsConfigFactory(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress) {
        this.bootstrapAddress = bootstrapAddress;
    }

    public StreamsConfig create(final ReplicationConsumePipeline<?, ?> pipeline) {
        final Map<String, Object> configsByNames = Map.of(
                APPLICATION_ID_CONFIG, pipeline.getId(),
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                PROCESSING_GUARANTEE_CONFIG, EXACTLY_ONCE_V2
        );
        return new StreamsConfig(configsByNames);
    }
}
