package by.aurorasoft.testapp.util;

import by.aurorasoft.testapp.crud.entity.ReplicatedAddressEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class ReplicatedAddressEntityUtil {

    public static void checkEquals(final ReplicatedAddressEntity expected, final ReplicatedAddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
    }
}
