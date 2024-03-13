package by.aurorasoft.replicator.model.produced;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;

@Value
public class UpdateProducedReplication<ID, DTO extends AbstractDto<ID>> implements ProducedReplication<ID> {
    DTO dto;

    @Override
    @JsonIgnore
    public ID getEntityId() {
        return dto.getId();
    }
}
