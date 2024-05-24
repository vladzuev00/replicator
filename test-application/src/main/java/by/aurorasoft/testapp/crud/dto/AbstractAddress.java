package by.aurorasoft.testapp.crud.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class AbstractAddress {
    private final Long id;
    private final String country;
    private final String city;
}
