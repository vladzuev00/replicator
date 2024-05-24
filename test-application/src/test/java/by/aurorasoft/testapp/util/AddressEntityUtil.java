package by.aurorasoft.testapp.util;

import by.aurorasoft.testapp.crud.v2.entity.AddressEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class AddressEntityUtil {

    public static void checkEquals(final AddressEntity expected, final AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCountry(), expected.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
    }
}
