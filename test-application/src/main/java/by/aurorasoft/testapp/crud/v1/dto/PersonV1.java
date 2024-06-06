package by.aurorasoft.testapp.crud.v1.dto;

import by.aurorasoft.testapp.crud.dto.Person;
import by.nhorushko.crudgeneric.domain.AbstractDto;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@SuppressWarnings("deprecation")
public final class PersonV1 implements AbstractDto, Person {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate birthDate;
    private AddressV1 address;
}
