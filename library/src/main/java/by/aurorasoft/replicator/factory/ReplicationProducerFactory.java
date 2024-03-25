package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.model.produced.ProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
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

    private <ID> ReplicationProducer<ID> create(final AbsServiceRUD<ID, ?, ?, ?, ?> service) {
        final String topicName = getTopicName(service);
        final KafkaTemplate<ID, ProducedReplication<ID>> kafkaTemplate = createKafkaTemplate(service);
        return new ReplicationProducer<>(topicName, kafkaTemplate);
    }

    private <ID> KafkaTemplate<ID, ProducedReplication<ID>> createKafkaTemplate(final AbsServiceRUD<ID, ?, ?, ?, ?> service) {
        final ProducerConfig producerConfig = getProducerConfig(service);
        final Map<String, Object> configsByKeys = createProducerConfigsByKeys(producerConfig);
        final ProducerFactory<ID, ProducedReplication<ID>> factory = new DefaultKafkaProducerFactory<>(configsByKeys);
        return new KafkaTemplate<>(factory);
    }

    private static String getTopicName(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return getTopicConfig(service).name();
    }

    private static ProducerConfig getProducerConfig(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return getReplicatedServiceAnnotation(service).producerConfig();
    }

    private static TopicConfig getTopicConfig(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return getReplicatedServiceAnnotation(service).topicConfig();
    }

    private static ReplicatedService getReplicatedServiceAnnotation(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return service.getClass().getAnnotation(ReplicatedService.class);
    }

    private Map<String, Object> createProducerConfigsByKeys(final ProducerConfig config) {
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
