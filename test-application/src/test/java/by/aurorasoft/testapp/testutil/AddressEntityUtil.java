package by.aurorasoft.testapp.testutil;

import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.aurorasoft.testapp.crud.entity.ReplicatedAddressEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UtilityClass
public final class AddressEntityUtil {

    public static void checkEquals(AddressEntity expected, AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCountry(), expected.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
    }

    public static void checkEquals(List<AddressEntity> expected, List<AddressEntity> actual) {
        EntityUtil.checkEquals(expected, actual, AddressEntityUtil::checkEquals);
    }
}
