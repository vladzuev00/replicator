package by.aurorasoft.testapp.crud.v1.dto;

import by.aurorasoft.testapp.crud.dto.AbstractPerson;
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
public final class Person extends AbstractPerson implements AbstractDto {
    private final Address address;

    @Builder
    public Person(final Long id,
                  final String name,
                  final String surname,
                  final String patronymic,
                  final LocalDate birthDate,
                  final Address address) {
        super(id, name, surname, patronymic, birthDate);
        this.address = address;
    }
}
