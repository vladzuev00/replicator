package by.aurorasoft.replicator.base.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

@Value
public class TestDto implements AbstractDto<Long> {
    Long id;
}
