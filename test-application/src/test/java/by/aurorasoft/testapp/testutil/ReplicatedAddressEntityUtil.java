package by.aurorasoft.testapp.testutil;

import by.aurorasoft.testapp.crud.entity.ReplicatedAddressEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UtilityClass
public final class ReplicatedAddressEntityUtil {

    public static void checkEquals(ReplicatedAddressEntity expected, ReplicatedAddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
    }

    public static void checkEquals(List<ReplicatedAddressEntity> expected, List<ReplicatedAddressEntity> actual) {
        EntityUtil.checkEquals(expected, actual, ReplicatedAddressEntityUtil::checkEquals);
    }
}
