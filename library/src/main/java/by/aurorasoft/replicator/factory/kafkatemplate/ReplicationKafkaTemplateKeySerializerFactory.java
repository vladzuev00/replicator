package by.aurorasoft.replicator.factory.kafkatemplate;

import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationKafkaTemplateKeySerializerFactory {

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public Serializer<Object> create() {
        return null;
//        return (Serializer<Object>) setting.getIdSerializer();
    }
}
