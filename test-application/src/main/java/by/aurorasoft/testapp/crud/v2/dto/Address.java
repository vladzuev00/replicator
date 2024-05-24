package by.aurorasoft.testapp.crud.v2.dto;

import by.aurorasoft.testapp.crud.dto.AddressDto;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class Address implements AbstractDto<Long>, AddressDto {
    Long id;
    String country;
    String city;
}
