package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.objectmapper.ReplicationObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Component
public final class ReplicationKafkaTemplateFactory {
    private final ReplicationObjectMapper objectMapper;
    private final String bootstrapAddress;

    public ReplicationKafkaTemplateFactory(ReplicationObjectMapper objectMapper,
                                           @Value("${spring.kafka.bootstrap-servers}") String bootstrapAddress) {
        this.objectMapper = objectMapper;
        this.bootstrapAddress = bootstrapAddress;
    }

    public KafkaTemplate<Object, ProducedReplication<?>> create(Producer config) {
        Map<String, Object> configsByKeys = createConfigsByKeys(config);
        Serializer<Object> keySerializer = createKeySerializer(config);
        JsonSerializer<ProducedReplication<?>> valueSerializer = createValueSerializer();
        var producerFactory = new DefaultKafkaProducerFactory<>(configsByKeys, keySerializer, valueSerializer);
        return new KafkaTemplate<>(producerFactory);
    }

    private Map<String, Object> createConfigsByKeys(Producer config) {
        return Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                BATCH_SIZE_CONFIG, config.batchSize(),
                LINGER_MS_CONFIG, config.lingerMs(),
                DELIVERY_TIMEOUT_MS_CONFIG, config.deliveryTimeoutMs(),
                ENABLE_IDEMPOTENCE_CONFIG, true
        );
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private Serializer<Object> createKeySerializer(Producer config) {
        return (Serializer<Object>) config.idSerializer().getConstructor().newInstance();
    }

    private JsonSerializer<ProducedReplication<?>> createValueSerializer() {
        return new JsonSerializer<>(objectMapper);
    }
}
