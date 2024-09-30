package by.aurorasoft.testapp.testutil;

import by.aurorasoft.testapp.crud.entity.ReplicatedAddressEntity;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;

@UtilityClass
public final class ReplicatedAddressEntityUtil {

    public static void assertEquals(ReplicatedAddressEntity expected, ReplicatedAddressEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getCountry(), actual.getCountry());
        Assertions.assertEquals(expected.getCity(), actual.getCity());
    }
}
