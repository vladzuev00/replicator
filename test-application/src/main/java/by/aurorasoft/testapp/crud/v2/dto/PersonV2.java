package by.aurorasoft.testapp.crud.v2.dto;

import by.aurorasoft.testapp.crud.dto.Person;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

import java.time.LocalDate;

@Value
public class PersonV2 implements AbstractDto<Long>, Person {
    Long id;
    String name;
    String surname;
    String patronymic;
    LocalDate birthDate;
    AddressV2 address;
}
