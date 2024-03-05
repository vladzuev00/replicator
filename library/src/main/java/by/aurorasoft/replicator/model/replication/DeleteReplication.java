package by.aurorasoft.replicator.model.replication;

import by.aurorasoft.replicator.model.ReplicationType;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import lombok.RequiredArgsConstructor;

import static by.aurorasoft.replicator.model.ReplicationType.DELETE;

@RequiredArgsConstructor
public final class DeleteReplication<ID, DTO extends AbstractDto<ID>> implements Replication<ID, DTO> {
    private final ID id;

    @Override
    public ID getEntityId() {
        return id;
    }

    @Override
    public void execute(final AbsServiceCRUD<ID, ?, DTO, ?> service) {
        service.delete(id);
    }

    @Override
    public ReplicationType getType() {
        return DELETE;
    }
}
