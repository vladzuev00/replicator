package by.aurorasoft.testapp.crud.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;

import java.time.LocalDate;

@Value
public class Person implements AbstractDto<Long> {
    Long id;
    String name;
    String surname;
    String patronymic;

    @JsonIgnore
    LocalDate birthDate;

    @JsonIgnore
    Address address;
}
