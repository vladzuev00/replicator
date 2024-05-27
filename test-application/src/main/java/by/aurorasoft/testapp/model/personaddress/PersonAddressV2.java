package by.aurorasoft.testapp.model.personaddress;

import by.aurorasoft.testapp.crud.v2.dto.AddressV2;
import lombok.Value;

@Value
public class PersonAddressV2 implements PersonAddress {
    AddressV2 address;
}
