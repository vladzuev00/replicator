package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.mapperwrapper.ProducedReplicationMapperWrapper;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerValueSerializerFactory {
    private final ProducedReplicationMapperWrapper mapperWrapper;

    public Serializer<ProducedReplication<?>> create() {
        return new JsonSerializer<>(mapperWrapper.getMapper());
    }
}
