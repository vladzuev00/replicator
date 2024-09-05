package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationKafkaTemplateKeySerializerFactory {

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public Serializer<Object> create(ProducerConfig config) {
        return (Serializer<Object>) config.idSerializer().getConstructor().newInstance();
//        return (Serializer<Object>) setting.getIdSerializer();
    }
}
