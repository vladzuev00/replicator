package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationKafkaTemplateKeySerializerFactory {

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public Serializer<Object> create(ReplicationProducerSetting<?, ?> setting) {
        return (Serializer<Object>) setting.getIdSerializer()
                .getConstructor()
                .newInstance();
    }
}
