package by.aurorasoft.testapp.crud.v1.dto;

import by.aurorasoft.testapp.crud.dto.AddressDto;
import by.nhorushko.crudgeneric.domain.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
@SuppressWarnings("deprecation")
public class Address implements AbstractDto, AddressDto {
    Long id;
    String country;
    String city;
}
