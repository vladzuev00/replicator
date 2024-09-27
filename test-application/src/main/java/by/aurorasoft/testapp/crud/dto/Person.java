package by.aurorasoft.testapp.crud.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
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
