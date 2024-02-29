package by.aurorasoft.replicator.base.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class TestDto implements AbstractDto<Long> {
    Long id;

    @JsonCreator
    public TestDto(@JsonProperty("id") final Long id) {
        this.id = id;
    }
}
