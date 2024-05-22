package by.aurorasoft.replicator.consuming.serde;

import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@RequiredArgsConstructor
public final class ConsumedReplicationSerde<E, ID> implements Serde<ConsumedReplication<E, ID>> {
    private final TypeReference<ConsumedReplication<E, ID>> replicationTypeReference;

    @Override
    public Serializer<ConsumedReplication<E, ID>> serializer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Deserializer<ConsumedReplication<E, ID>> deserializer() {
        return new JsonDeserializer<>(replicationTypeReference, false);
    }
}
