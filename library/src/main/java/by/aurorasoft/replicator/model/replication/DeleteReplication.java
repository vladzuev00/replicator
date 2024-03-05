package by.aurorasoft.replicator.model.replication;

import by.aurorasoft.replicator.model.ReplicationType;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static by.aurorasoft.replicator.model.ReplicationType.DELETE;

@RequiredArgsConstructor
@Getter
public final class DeleteReplication<ID, DTO extends AbstractDto<ID>> implements Replication<ID, DTO> {
    private final ID entityId;

    @Override
    public void execute(final AbsServiceCRUD<ID, ?, DTO, ?> service) {
        service.delete(entityId);
    }

    @Override
    public ReplicationType getType() {
        return DELETE;
    }
}
