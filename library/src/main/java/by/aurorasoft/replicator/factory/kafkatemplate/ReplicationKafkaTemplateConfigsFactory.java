package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Component
public final class ReplicationKafkaTemplateConfigsFactory {
    private final String bootstrapAddress;

    public ReplicationKafkaTemplateConfigsFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapAddress) {
        this.bootstrapAddress = bootstrapAddress;
    }

    public Map<String, Object> create(ReplicationProduceSetting<?, ?> setting) {
        return Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                BATCH_SIZE_CONFIG, setting.getBatchSize(),
                LINGER_MS_CONFIG, setting.getLingerMs(),
                DELIVERY_TIMEOUT_MS_CONFIG, setting.getDeliveryTimeoutMs(),
                ENABLE_IDEMPOTENCE_CONFIG, true
        );
    }
}
