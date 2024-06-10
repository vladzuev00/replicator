package by.aurorasoft.testapp.testutil;

import by.aurorasoft.testapp.crud.entity.PersonEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class PersonEntityUtil {

    public void checkEquals(final PersonEntity expected, final PersonEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getPatronymic(), actual.getPatronymic());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getAddress().getId(), actual.getAddress().getId());
    }
}
