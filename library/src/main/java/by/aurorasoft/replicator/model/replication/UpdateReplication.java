package by.aurorasoft.replicator.model.replication;

import by.aurorasoft.replicator.model.ReplicationType;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import lombok.RequiredArgsConstructor;

import static by.aurorasoft.replicator.model.ReplicationType.UPDATE;

@RequiredArgsConstructor
public final class UpdateReplication<ID, DTO extends AbstractDto<ID>> implements Replication<ID, DTO> {
    private final DTO dto;

    @Override
    public ID getEntityId() {
        return dto.getId();
    }

    @Override
    public void execute(final AbsServiceCRUD<ID, ?, DTO, ?> service) {
        service.update(dto);
    }

    @Override
    public ReplicationType getType() {
        return UPDATE;
    }
}
