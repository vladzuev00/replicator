package by.aurorasoft.testapp.crud.v1.dto;

import by.nhorushko.crudgeneric.domain.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
@SuppressWarnings("deprecation")
public class Address implements AbstractDto {
    Long id;
    String country;
    String city;
}
