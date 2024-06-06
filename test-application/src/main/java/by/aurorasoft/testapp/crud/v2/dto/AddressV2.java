package by.aurorasoft.testapp.crud.v2.dto;

import by.aurorasoft.testapp.crud.dto.Address;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

@Value
public class AddressV2 implements AbstractDto<Long>, Address {
    Long id;
    String country;
    String city;
}
