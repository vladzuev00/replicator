package by.aurorasoft.testapp.testutil;

import by.aurorasoft.testapp.crud.entity.AddressEntity;
import lombok.experimental.UtilityClass;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UtilityClass
public final class AddressEntityUtil {

    public static void checkEquals(AddressEntity expected, AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCountry(), expected.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
    }
}
