package by.aurorasoft.replicator.model.produced;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

import static by.aurorasoft.replicator.util.TransportNameUtil.DELETE_ENTITY_ID;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class DeleteProducedReplication<ID> extends ProducedReplication<ID> {

    @JsonProperty(DELETE_ENTITY_ID)
    private final ID entityId;

    public DeleteProducedReplication(final UUID uuid, final ID entityId) {
        super(uuid);
        this.entityId = entityId;
    }
}
