package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationKafkaTemplateKeySerializerFactory {

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public Serializer<Object> create(ReplicationProduceSetting<?, ?> setting) {
        return (Serializer<Object>) setting.getIdSerializer();
    }
}
