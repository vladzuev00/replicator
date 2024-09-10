package by.aurorasoft.testapp.crud.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@Builder
@AllArgsConstructor
@FieldNameConstants
public class Address implements AbstractDto<Long> {
    Long id;

    @JsonIgnore
    String country;

    String city;
}
