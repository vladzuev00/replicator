package by.aurorasoft.replicator.factory.kafkastreams;

import by.aurorasoft.replicator.model.setting.ReplicationConsumerSetting;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;

@Component
public final class ReplicationStreamsConfigFactory {
    private final String bootstrapAddress;

    public ReplicationStreamsConfigFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapAddress) {
        this.bootstrapAddress = bootstrapAddress;
    }

    public StreamsConfig create(ReplicationConsumerSetting<?, ?> setting) {
        Map<String, Object> configsByNames = Map.of(
                APPLICATION_ID_CONFIG, setting.getTopic(),
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                PROCESSING_GUARANTEE_CONFIG, EXACTLY_ONCE_V2
        );
        return new StreamsConfig(configsByNames);
    }
}
