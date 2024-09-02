package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationKafkaTemplateFactory {
    private final ReplicationKafkaTemplateConfigsFactory configsFactory;
    private final ReplicationKafkaTemplateKeySerializerFactory keySerializerFactory;
    private final ReplicationKafkaTemplateValueSerializerFactory valueSerializerFactory;

    public KafkaTemplate<Object, ProducedReplication<?>> create(ProducerConfig config) {
        throw new UnsupportedOperationException();
//        Map<String, Object> configsByKeys = configsFactory.create(setting);
//        Serializer<Object> keySerializer = keySerializerFactory.create(setting);
//        Serializer<ProducedReplication<?>> valueSerializer = valueSerializerFactory.create();
//        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configsByKeys, keySerializer, valueSerializer));
    }
}
