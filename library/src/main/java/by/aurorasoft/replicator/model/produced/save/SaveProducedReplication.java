package by.aurorasoft.replicator.model.produced.save;

import by.aurorasoft.replicator.model.produced.ProducedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class SaveProducedReplication<ID, DTO extends AbstractDto<ID>> implements ProducedReplication<ID> {

    @JsonProperty("body")
    private final DTO dto;

    @Override
    @JsonIgnore
    public final ID getEntityId() {
        return dto.getId();
    }
}
