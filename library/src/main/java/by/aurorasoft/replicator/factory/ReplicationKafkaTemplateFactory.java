package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.objectmapper.ReplicationObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public final class ReplicationKafkaTemplateFactory {
    private final ReplicationKafkaTemplateConfigsFactory configsFactory;
    private final ReplicationObjectMapper objectMapper;

    public KafkaTemplate<Object, ProducedReplication<?>> create(Producer config) {
        Map<String, Object> configsByKeys = configsFactory.create(config);
        Serializer<Object> keySerializer = createKeySerializer(config);
        JsonSerializer<ProducedReplication<?>> valueSerializer = createValueSerializer();
        var producerFactory = new DefaultKafkaProducerFactory<>(configsByKeys, keySerializer, valueSerializer);
        return new KafkaTemplate<>(producerFactory);
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
