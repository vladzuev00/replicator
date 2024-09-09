package by.aurorasoft.testapp.crud.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Value
@FieldNameConstants
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
