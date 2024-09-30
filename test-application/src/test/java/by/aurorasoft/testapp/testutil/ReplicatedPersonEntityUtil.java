package by.aurorasoft.testapp.testutil;

import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;

@UtilityClass
public final class ReplicatedPersonEntityUtil {

    public static void assertEquals(ReplicatedPersonEntity expected, ReplicatedPersonEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assertions.assertEquals(expected.getAddress().getId(), actual.getAddress().getId());
    }
}
