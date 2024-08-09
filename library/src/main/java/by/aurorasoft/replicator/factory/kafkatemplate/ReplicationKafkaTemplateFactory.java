package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting;
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

    public KafkaTemplate<Object, ProducedReplication<?>> create(ReplicationProduceSetting<?, ?> setting) {
        Map<String, Object> configsByKeys = configsFactory.create(setting);
        Serializer<Object> keySerializer = keySerializerFactory.create(setting);
        Serializer<ProducedReplication<?>> valueSerializer = valueSerializerFactory.create();
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configsByKeys, keySerializer, valueSerializer));
    }
}
