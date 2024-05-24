package by.aurorasoft.testapp.util;

import by.aurorasoft.testapp.crud.v2.entity.ReplicatedAddressEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class ReplicatedAddressEntityUtil {

    public static void checkEquals(final ReplicatedAddressEntity expected, final ReplicatedAddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
    }

    public static void checkEquals(final List<ReplicatedAddressEntity> expected,
                                   final List<ReplicatedAddressEntity> actual) {
        EntityUtil.checkEquals(expected, actual, ReplicatedAddressEntityUtil::checkEquals);
    }
}
