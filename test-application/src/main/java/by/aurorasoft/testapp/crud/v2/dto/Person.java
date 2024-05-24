package by.aurorasoft.testapp.crud.v2.dto;

import by.aurorasoft.testapp.crud.dto.AbstractPerson;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.*;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class Person extends AbstractPerson implements AbstractDto<Long> {
    private final Address address;

    @Builder
    public Person(Long id, String name, String surname, String patronymic, LocalDate birthDate, Address address) {
        super(id, name, surname, patronymic, birthDate);
        this.address = address;
    }
}
