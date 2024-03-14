package by.aurorasoft.replicator.consuming.deserializer;

import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public final class ReplicationDeserializer<ID, E extends AbstractEntity<ID>> extends JsonDeserializer<ConsumedReplication<ID, E>> {

    public ReplicationDeserializer(final ObjectMapper objectMapper, final TypeReference<ConsumedReplication<ID, E>> typeReference) {
        super(typeReference, objectMapper, false);
    }
}
