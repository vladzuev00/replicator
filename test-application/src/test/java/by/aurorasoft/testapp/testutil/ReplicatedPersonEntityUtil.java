package by.aurorasoft.testapp.testutil;

import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class ReplicatedPersonEntityUtil {

    public void checkEquals(final ReplicatedPersonEntity expected, final ReplicatedPersonEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getAddress().getId(), actual.getAddress().getId());
    }
}
