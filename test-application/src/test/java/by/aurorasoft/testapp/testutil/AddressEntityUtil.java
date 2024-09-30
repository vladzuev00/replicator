package by.aurorasoft.testapp.testutil;

import by.aurorasoft.testapp.crud.entity.AddressEntity;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;

@UtilityClass
public final class AddressEntityUtil {

    public static void assertEquals(AddressEntity expected, AddressEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getCountry(), expected.getCountry());
        Assertions.assertEquals(expected.getCity(), actual.getCity());
    }
}
