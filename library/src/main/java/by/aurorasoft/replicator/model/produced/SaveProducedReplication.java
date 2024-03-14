package by.aurorasoft.replicator.model.produced;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

import static by.aurorasoft.replicator.util.TransportNameUtil.BODY_NAME;

@Getter
public final class SaveProducedReplication<ID, DTO extends AbstractDto<ID>> extends ProducedReplication<ID> {

    @JsonProperty(BODY_NAME)
    private final DTO dto;

    public SaveProducedReplication(final UUID uuid, final DTO dto) {
        super(uuid);
        this.dto = dto;
    }

    @Override
    @JsonIgnore
    public ID getEntityId() {
        return dto.getId();
    }
}
