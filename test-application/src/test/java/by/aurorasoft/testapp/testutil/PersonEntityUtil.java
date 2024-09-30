package by.aurorasoft.testapp.testutil;

import by.aurorasoft.testapp.crud.entity.PersonEntity;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;

@UtilityClass
public final class PersonEntityUtil {

    public static void assertEquals(PersonEntity expected, PersonEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getPatronymic(), actual.getPatronymic());
        Assertions.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assertions.assertEquals(expected.getAddress().getId(), actual.getAddress().getId());
    }
}
