package by.aurorasoft.testapp.crud.dto;

import java.time.LocalDate;

public interface Person {
    Long getId();

    String getName();

    String getSurname();

    String getPatronymic();

    LocalDate getBirthDate();
    Address getAddress();
}
