package by.aurorasoft.replicator.consuming.deserializer;

import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public final class ReplicationDeserializer<ID, DTO extends AbstractDto<ID>>
        extends JsonDeserializer<Replication<ID, DTO>> {

    public ReplicationDeserializer(final ObjectMapper objectMapper,
                                   final TypeReference<Replication<ID, DTO>> typeReference) {
        super(typeReference, objectMapper, false);
    }
}
