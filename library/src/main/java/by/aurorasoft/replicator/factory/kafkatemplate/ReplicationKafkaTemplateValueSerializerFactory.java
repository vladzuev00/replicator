package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationKafkaTemplateValueSerializerFactory {
//    private final ProducedReplicationMapperWrapper mapperWrapper;

    public Serializer<ProducedReplication<?>> create() {
        return null;
//        return new JsonSerializer<>(mapperWrapper.getMapper());
    }
}
