package by.aurorasoft.replicator.model.replication;

import by.aurorasoft.replicator.model.ReplicationType;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;

import static by.aurorasoft.replicator.model.ReplicationType.DELETE;

public final class DeleteReplication<ID, DTO extends AbstractDto<ID>> extends Replication<ID, DTO> {

    public DeleteReplication(final DTO dto) {
        super(dto);
    }

    @Override
    public ReplicationType getType() {
        return DELETE;
    }

    @Override
    protected void execute(final AbsServiceCRUD<ID, ?, DTO, ?> service, final DTO dto) {
        service.delete(dto.getId());
    }
}
