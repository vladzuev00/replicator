package by.aurorasoft.replicator.model.produced;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import static by.aurorasoft.replicator.util.TransportConfigUtil.SAVE_BODY;

@Value
public class SaveProducedReplication<ID, DTO extends AbstractDto<ID>> implements ProducedReplication<ID> {

    @JsonProperty(SAVE_BODY)
    DTO dto;

    @Override
    @JsonIgnore
    public ID getEntityId() {
        return dto.getId();
    }
}
