package by.aurorasoft.replicator.factory.kafkastreams;

import by.aurorasoft.replicator.model.setting.ReplicationConsumerSetting;
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

    public KafkaStreams create(ReplicationConsumerSetting<?, ?> setting) {
        Topology topology = topologyFactory.create(setting);
        StreamsConfig config = configFactory.create(setting);
        return streamsFactory.create(topology, config);
    }
}
