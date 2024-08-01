package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationProducerKeySerializerFactory {

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public Serializer<Object> create(Producer config) {
        return (Serializer<Object>) config.idSerializer()
                .getConstructor()
                .newInstance();
    }
}
