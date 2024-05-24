package by.aurorasoft.testapp.crud.v1.dto;

import by.aurorasoft.testapp.crud.dto.Person;
import by.nhorushko.crudgeneric.domain.AbstractDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@SuppressWarnings("deprecation")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class PersonV1 extends Person implements AbstractDto {
    private final AddressV1 address;

    @Builder
    public PersonV1(final Long id,
                    final String name,
                    final String surname,
                    final String patronymic,
                    final LocalDate birthDate,
                    final AddressV1 address) {
        super(id, name, surname, patronymic, birthDate);
        this.address = address;
    }
}
