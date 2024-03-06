package by.aurorasoft.replicator.model;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public final class UpdateReplication<ID, DTO extends AbstractDto<ID>> implements Replication<ID, DTO> {
    private final DTO dto;

    @JsonCreator
    public UpdateReplication(@JsonProperty("dto") final DTO dto) {
        this.dto = dto;
    }

    @Override
    @JsonIgnore
    public ID getEntityId() {
        return dto.getId();
    }

    @Override
    public void execute(final AbsServiceCRUD<ID, ?, DTO, ?> service) {
        service.update(dto);
    }
}