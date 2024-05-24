package by.aurorasoft.testapp.crud.v2.dto;

import by.aurorasoft.testapp.crud.dto.Person;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.*;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class PersonV2 extends Person implements AbstractDto<Long> {
    private final AddressV2 address;

    @Builder
    public PersonV2(Long id, String name, String surname, String patronymic, LocalDate birthDate, AddressV2 address) {
        super(id, name, surname, patronymic, birthDate);
        this.address = address;
    }
}
