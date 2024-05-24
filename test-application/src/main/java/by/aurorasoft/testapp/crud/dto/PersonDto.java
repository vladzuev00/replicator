package by.aurorasoft.testapp.crud.dto;

import java.time.LocalDate;

public interface PersonDto {
    Long getId();

    String getName();

    String getSurname();

    String getPatronymic();

    LocalDate getBirthDate();

    AddressDto getAddress();
}
