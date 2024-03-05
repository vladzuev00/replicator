package by.aurorasoft.replicator.model.replication;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public final class DeleteReplication<ID, DTO extends AbstractDto<ID>> implements Replication<ID, DTO> {
    private final ID entityId;

    @JsonCreator
    public DeleteReplication(@JsonProperty("entityId") final ID entityId) {
        this.entityId = entityId;
    }

    @Override
    public ID getEntityId() {
        return entityId;
    }

    @Override
    public void execute(final AbsServiceCRUD<ID, ?, DTO, ?> service) {
        service.delete(entityId);
    }
}
