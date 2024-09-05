package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public final class ReplicationKafkaTemplateFactory {
    private final ReplicationKafkaTemplateConfigsFactory configsFactory;
    private final ReplicationKafkaTemplateKeySerializerFactory keySerializerFactory;
    private final ReplicationKafkaTemplateValueSerializerFactory valueSerializerFactory;

    public KafkaTemplate<Object, ProducedReplication<?>> create(ProducerConfig config) {
        Map<String, Object> configsByKeys = configsFactory.create(config);
        Serializer<Object> keySerializer = keySerializerFactory.create(config);
        Serializer<ProducedReplication<?>> valueSerializer = valueSerializerFactory.create();
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configsByKeys, keySerializer, valueSerializer));
    }
}
