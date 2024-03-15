package by.aurorasoft.replicator.model.produced;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import static by.aurorasoft.replicator.util.TransportNameUtil.BODY_NAME;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class SaveProducedReplication<ID, DTO extends AbstractDto<ID>> implements ProducedReplication<ID> {

    @JsonProperty(BODY_NAME)
    private final DTO dto;

    @Override
    @JsonIgnore
    public ID getEntityId() {
        return dto.getId();
    }
}
