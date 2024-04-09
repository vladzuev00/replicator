package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.stereotype.Component;

import static java.lang.Runtime.getRuntime;
import static org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;

@Component
@RequiredArgsConstructor
public final class ReplicationKafkaStreamsFactory {
    private final ReplicationTopologyFactory topologyFactory;
    private final ReplicationStreamsConfigFactory configFactory;
    private final KafkaStreamsFactory streamsFactory;

    public KafkaStreams create(final ReplicationConsumePipeline<?, ?> pipeline) {
        final Topology topology = topologyFactory.create(pipeline);
        final StreamsConfig config = configFactory.create(pipeline);
        return streamsFactory.create(topology, config);
    }
}
