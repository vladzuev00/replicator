package by.aurorasoft.testapp.crud.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
public class ReplicatedPerson implements AbstractDto<Long> {
    Long id;
    String name;
    String surname;
    LocalDate birthDate;

    @Builder
    @JsonCreator
    public ReplicatedPerson(@JsonProperty("id") final Long id,
                            @JsonProperty("name") final String name,
                            @JsonProperty("surname") final String surname,
                            @JsonProperty("birthDate") final LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
    }
}
