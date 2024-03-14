package by.aurorasoft.replicator.model.produced;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

import static by.aurorasoft.replicator.util.TransportNameUtil.ENTITY_ID_NAME;

@Getter
public final class DeleteProducedReplication<ID> extends ProducedReplication<ID> {

    @JsonProperty(ENTITY_ID_NAME)
    private final ID entityId;

    public DeleteProducedReplication(final UUID uuid, final ID entityId) {
        super(uuid);
        this.entityId = entityId;
    }
}
