package by.aurorasoft.testapp.crud.v2.dto;

import by.aurorasoft.testapp.crud.dto.PersonDto;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor
@Builder
public class Person implements AbstractDto<Long>, PersonDto {
    Long id;
    String name;
    String surname;
    String patronymic;
    LocalDate birthDate;
    Address address;
}
