package by.aurorasoft.testapp.crud.v2.dto;

import by.aurorasoft.testapp.crud.dto.Address;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class AddressV2 extends Address implements AbstractDto<Long> {

    @Builder
    public AddressV2(final Long id, final String country, final String city) {
        super(id, country, city);
    }
}
