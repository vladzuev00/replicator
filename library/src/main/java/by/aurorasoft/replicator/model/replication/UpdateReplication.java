package by.aurorasoft.replicator.model.replication;

import by.aurorasoft.replicator.model.ReplicationType;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;

import static by.aurorasoft.replicator.model.ReplicationType.UPDATE;

public final class UpdateReplication<ID, DTO extends AbstractDto<ID>> extends Replication<ID, DTO> {

    public UpdateReplication(final DTO dto) {
        super(dto);
    }

    @Override
    public ReplicationType getType() {
        return UPDATE;
    }

    @Override
    protected void execute(final AbsServiceCRUD<ID, ?, DTO, ?> service, final DTO dto) {
        service.update(dto);
    }
}
