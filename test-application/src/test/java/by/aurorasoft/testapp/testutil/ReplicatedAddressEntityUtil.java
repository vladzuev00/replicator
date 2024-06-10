package by.aurorasoft.testapp.testutil;

import by.aurorasoft.testapp.crud.entity.ReplicatedAddressEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class ReplicatedAddressEntityUtil {

    public void checkEquals(final ReplicatedAddressEntity expected, final ReplicatedAddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getCity(), actual.getCity());
    }

    public void checkEquals(final List<ReplicatedAddressEntity> expected, final List<ReplicatedAddressEntity> actual) {
        EntityUtil.checkEquals(expected, actual, ReplicatedAddressEntityUtil::checkEquals);
    }
}
