package by.aurorasoft.testapp.model;

import by.aurorasoft.testapp.crud.v2.dto.AddressV2;
import lombok.Value;

//TODO: do with AddressV1 and AddressV2
@Value
public class PersonAddress {
    AddressV2 address;
}
