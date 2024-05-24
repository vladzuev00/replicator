package by.aurorasoft.testapp.crud.v1.dto;

import by.nhorushko.crudgeneric.domain.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor
@Builder
@SuppressWarnings("deprecation")
public class Person implements AbstractDto {
    Long id;
    String name;
    String surname;
    String patronymic;
    LocalDate birthDate;
    Address address;
}
