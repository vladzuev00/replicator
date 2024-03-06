package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consumer.KafkaReplicationConsumerConfig;
import by.aurorasoft.replicator.deserializer.ReplicationDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationDeserializerFactory {
    private final ObjectMapper objectMapper;

    public ReplicationDeserializer<?, ?> createDeserializer(final KafkaReplicationConsumerConfig<?, ?> config) {
        return new ReplicationDeserializer<>(config.getReplicationTypeReference(), objectMapper);
    }
}
