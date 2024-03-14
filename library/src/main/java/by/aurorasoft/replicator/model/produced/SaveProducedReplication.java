package by.aurorasoft.replicator.model.produced;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

import static by.aurorasoft.replicator.util.TransportNameUtil.SAVE_BODY;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class SaveProducedReplication<ID, DTO extends AbstractDto<ID>> extends ProducedReplication<ID> {

    @JsonProperty(SAVE_BODY)
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
