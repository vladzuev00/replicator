package by.aurorasoft.testapp.crud.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class AbstractPerson {
    private final Long id;
    private final String name;
    private final String surname;
    private final String patronymic;
    private final LocalDate birthDate;

    public abstract AbstractAddress getAddress();
}
