package by.aurorasoft.testapp.crud.v1.dto;

import by.aurorasoft.testapp.crud.dto.Address;
import by.nhorushko.crudgeneric.domain.AbstractDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@SuppressWarnings("deprecation")
public final class AddressV1 implements AbstractDto, Address {
    private Long id;
    private String country;
    private String city;
}
