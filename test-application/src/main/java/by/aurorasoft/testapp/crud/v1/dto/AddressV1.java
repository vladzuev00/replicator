package by.aurorasoft.testapp.crud.v1.dto;

import by.aurorasoft.testapp.crud.dto.Address;
import by.nhorushko.crudgeneric.domain.AbstractDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("deprecation")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class AddressV1 extends Address implements AbstractDto {

    @Builder
    public AddressV1(final Long id, final String country, final String city) {
        super(id, country, city);
    }
}
