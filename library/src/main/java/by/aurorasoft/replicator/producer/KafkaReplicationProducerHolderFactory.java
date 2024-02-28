package by.aurorasoft.replicator.producer;

import by.aurorasoft.kafka.serialize.JsonPojoSerializer;
import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.config.ReplicationProducerConfig;
import by.aurorasoft.replicator.holder.KafkaReplicationProducerHolder;
import by.aurorasoft.replicator.holder.ReplicatedServiceHolder;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Component
public final class KafkaReplicationProducerHolderFactory {
    private final ReplicatedServiceHolder replicatedServiceHolder;
    private final ObjectMapper objectMapper;
    private final ReplicationProducerConfig producerConfig;
    private final String bootstrapAddress;

    public KafkaReplicationProducerHolderFactory(final ReplicatedServiceHolder replicatedServiceHolder,
                                                 final ObjectMapper objectMapper,
                                                 final ReplicationProducerConfig producerConfig,
                                                 @Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress) {
        this.replicatedServiceHolder = replicatedServiceHolder;
        this.objectMapper = objectMapper;
        this.producerConfig = producerConfig;
        this.bootstrapAddress = bootstrapAddress;
    }

    public KafkaReplicationProducerHolder create() {
        return replicatedServiceHolder.getServices()
                .stream()
                .collect(collectingAndThen(toMap(identity(), this::createProducer), KafkaReplicationProducerHolder::new));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private KafkaReplicationProducer<?, ?> createProducer(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        final ReplicatedService annotation = service.getClass().getAnnotation(ReplicatedService.class);
        final Map<String, Object> configsByKeys = createProducerConfigsByKeys(annotation.idSerializer());
        final ProducerFactory producerFactory = new DefaultKafkaProducerFactory(configsByKeys);
        final KafkaTemplate kafkaTemplate = new KafkaTemplate(producerFactory);
        return new KafkaReplicationProducer<>(annotation.topicName(), kafkaTemplate, objectMapper);
    }

    private Map<String, Object> createProducerConfigsByKeys(final Class<? extends Serializer<?>> idSerializerType) {
        return Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                KEY_SERIALIZER_CLASS_CONFIG, idSerializerType,
                VALUE_SERIALIZER_CLASS_CONFIG, JsonPojoSerializer.class,
                BATCH_SIZE_CONFIG, producerConfig.getBatchSize(),
                LINGER_MS_CONFIG, producerConfig.getLingerMs(),
                DELIVERY_TIMEOUT_MS_CONFIG, producerConfig.getDeliveryTimeoutMs()
        );
    }
}
