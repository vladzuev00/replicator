package by.aurorasoft.replicator.v2.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

@Value
public class TestV2Dto implements AbstractDto<Long> {
    Long id;
}
