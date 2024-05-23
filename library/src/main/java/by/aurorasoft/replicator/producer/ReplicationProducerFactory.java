package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Component
public final class ReplicationProducerFactory {
    private final String bootstrapAddress;

    public ReplicationProducerFactory(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress) {
        this.bootstrapAddress = bootstrapAddress;
    }

    public ReplicationProducer create(final Object service) {
        final String topicName = getTopicName(service);
        final KafkaTemplate<Object, ProducedReplication> kafkaTemplate = createKafkaTemplate(service);
        return new ReplicationProducer(topicName, kafkaTemplate);
    }

    private String getTopicName(final Object service) {
        return getTopicConfig(service).name();
    }

    private KafkaTemplate<Object, ProducedReplication> createKafkaTemplate(final Object service) {
        final ProducerConfig producerConfig = getProducerConfig(service);
        final Map<String, Object> configsByKeys = createConfigsByKeys(producerConfig);
        final ProducerFactory<Object, ProducedReplication> factory = new DefaultKafkaProducerFactory<>(configsByKeys);
        return new KafkaTemplate<>(factory);
    }

    private ProducerConfig getProducerConfig(final Object service) {
        return getReplicatedServiceAnnotation(service).producerConfig();
    }

    private TopicConfig getTopicConfig(final Object service) {
        return getReplicatedServiceAnnotation(service).topicConfig();
    }

    private ReplicatedService getReplicatedServiceAnnotation(final Object service) {
        return service.getClass().getAnnotation(ReplicatedService.class);
    }

    private Map<String, Object> createConfigsByKeys(final ProducerConfig config) {
        return Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                KEY_SERIALIZER_CLASS_CONFIG, config.idSerializer(),
                VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                BATCH_SIZE_CONFIG, config.batchSize(),
                LINGER_MS_CONFIG, config.lingerMs(),
                DELIVERY_TIMEOUT_MS_CONFIG, config.deliveryTimeoutMs(),
                ENABLE_IDEMPOTENCE_CONFIG, true
        );
    }
}
