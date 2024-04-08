package by.aurorasoft.testapp.util;

import by.aurorasoft.testapp.crud.entity.PersonEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class PersonEntityUtil {

    public static void checkEquals(final PersonEntity expected, final PersonEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getPatronymic(), actual.getPatronymic());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getAddress().getId(), actual.getAddress().getId());
    }

    public static void checkEquals(final List<PersonEntity> expected, final List<PersonEntity> actual) {
        EntityUtil.checkEquals(expected, actual, PersonEntityUtil::checkEquals);
    }
}
