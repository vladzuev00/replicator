package by.aurorasoft.testapp.crud.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

@Value
public class Address implements AbstractDto<Long> {
    Long id;
    String country;
    String city;
}
