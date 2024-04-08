package by.aurorasoft.testapp.util;

import by.aurorasoft.testapp.crud.entity.AddressEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class AddressEntityUtil {

    public static void checkEquals(final AddressEntity expected, final AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCountry(), expected.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
    }

    public static void checkEquals(final List<AddressEntity> expected, final List<AddressEntity> actual) {
        EntityUtil.checkEquals(expected, actual, AddressEntityUtil::checkEquals);
    }
}
