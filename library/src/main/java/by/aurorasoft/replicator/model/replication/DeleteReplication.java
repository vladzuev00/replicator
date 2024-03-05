package by.aurorasoft.replicator.model.replication;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteReplication<ID, DTO extends AbstractDto<ID>> implements Replication<ID, DTO> {
    private final ID entityId;

    @Override
    public ID getEntityId() {
        return entityId;
    }

    @Override
    public void execute(final AbsServiceCRUD<ID, ?, DTO, ?> service) {
        service.delete(entityId);
    }
}
