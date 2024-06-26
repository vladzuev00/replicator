package by.aurorasoft.replicator.consuming.starter.factory;

import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.stereotype.Component;

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
