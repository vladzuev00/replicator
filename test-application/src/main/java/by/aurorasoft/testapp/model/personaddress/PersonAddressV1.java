package by.aurorasoft.testapp.model.personaddress;

import by.aurorasoft.testapp.crud.v1.dto.AddressV1;
import lombok.Value;

@Value
public class PersonAddressV1 implements PersonAddress {
    AddressV1 address;
}
