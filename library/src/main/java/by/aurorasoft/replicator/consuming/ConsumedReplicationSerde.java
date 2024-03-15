package by.aurorasoft.replicator.consuming;

import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@RequiredArgsConstructor
public final class ConsumedReplicationSerde<ID, E extends AbstractEntity<ID>> implements Serde<ConsumedReplication<ID, E>> {
    private final TypeReference<ConsumedReplication<ID, E>> replicationTypeReference;

    @Override
    public Serializer<ConsumedReplication<ID, E>> serializer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Deserializer<ConsumedReplication<ID, E>> deserializer() {
        return new JsonDeserializer<>(replicationTypeReference, false);
    }
}
