package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Component
public final class ReplicationProducerConfigsFactory {
    private final String bootstrapAddress;

    public ReplicationProducerConfigsFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapAddress) {
        this.bootstrapAddress = bootstrapAddress;
    }

    public Map<String, Object> create(Producer config) {
        return Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                BATCH_SIZE_CONFIG, config.batchSize(),
                LINGER_MS_CONFIG, config.lingerMs(),
                DELIVERY_TIMEOUT_MS_CONFIG, config.deliveryTimeoutMs(),
                ENABLE_IDEMPOTENCE_CONFIG, true
        );
    }
}
