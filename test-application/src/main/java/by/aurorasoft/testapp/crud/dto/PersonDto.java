package by.aurorasoft.testapp.crud.dto;

import by.aurorasoft.testapp.crud.v2.dto.Address;

import java.time.LocalDate;

public interface PersonDto {
    Long getId();
    String getName();
    String getSurname();
    String getPatronymic();
    LocalDate getBirthDate();
    Address getAddress();
}
