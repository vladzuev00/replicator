package by.aurorasoft.replicator.model.produced;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;

@Value
public class SaveProducedReplication<ENTITY_ID, DTO extends AbstractDto<ENTITY_ID>> implements ProducedReplication<ENTITY_ID> {
    DTO dto;

    @Override
    @JsonIgnore
    public ENTITY_ID getEntityId() {
        return dto.getId();
    }
}
