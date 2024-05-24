package by.aurorasoft.testapp.crud.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
@SuppressWarnings("deprecation")
public class Address implements AbstractDto<Long>, by.nhorushko.crudgeneric.domain.AbstractDto {
    Long id;
    String country;
    String city;
}
