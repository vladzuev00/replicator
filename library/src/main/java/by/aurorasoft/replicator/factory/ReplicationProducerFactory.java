package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
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
    private final ObjectMapper objectMapper;
    private final String bootstrapAddress;

    public ReplicationProducerFactory(ObjectMapper objectMapper,
                                      @Value("${spring.kafka.bootstrap-servers}") String bootstrapAddress) {
        this.objectMapper = objectMapper;
        this.bootstrapAddress = bootstrapAddress;
    }

    public ReplicationProducer create(Object service) {
        String topicName = getTopicName(service);
        KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate = createKafkaTemplate(service);
        return new ReplicationProducer(topicName, kafkaTemplate);
    }

    private String getTopicName(Object service) {
        return getTopicConfig(service).name();
    }

    @SneakyThrows
    private KafkaTemplate<Object, ProducedReplication<?>> createKafkaTemplate(Object service) {
        ProducerConfig producerConfig = getProducerConfig(service);
        Map<String, Object> configsByKeys = createConfigsByKeys(producerConfig);
        ProducerFactory<Object, ProducedReplication<?>> factory = new DefaultKafkaProducerFactory<>(configsByKeys, (Serializer<Object>) producerConfig.idSerializer().getConstructor().newInstance(), new JsonSerializer<>(objectMapper));
        return new KafkaTemplate<>(factory);
    }

    private ProducerConfig getProducerConfig(Object service) {
        return getReplicatedServiceAnnotation(service).producerConfig();
    }

    private TopicConfig getTopicConfig(Object service) {
        return getReplicatedServiceAnnotation(service).topicConfig();
    }

    private ReplicatedService getReplicatedServiceAnnotation(Object service) {
        return service.getClass().getAnnotation(ReplicatedService.class);
    }

    private Map<String, Object> createConfigsByKeys(ProducerConfig config) {
        return Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
//                KEY_SERIALIZER_CLASS_CONFIG, config.idSerializer(),
//                VALUE_SERIALIZER_CLASS_CONFIG, new JsonSerializer<>(objectMapper),
                BATCH_SIZE_CONFIG, config.batchSize(),
                LINGER_MS_CONFIG, config.lingerMs(),
                DELIVERY_TIMEOUT_MS_CONFIG, config.deliveryTimeoutMs(),
                ENABLE_IDEMPOTENCE_CONFIG, true
        );
    }
}
