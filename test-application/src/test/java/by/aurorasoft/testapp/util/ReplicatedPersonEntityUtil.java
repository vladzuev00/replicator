package by.aurorasoft.testapp.util;

import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;

@UtilityClass
public final class ReplicatedPersonEntityUtil {

    public static void checkEquals(final ReplicatedPersonEntity expected, final ReplicatedPersonEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getBirthDate(), actual.getBirthDate());
        assertEquals(expected.getAddress().getId(), actual.getAddress().getId());
    }

    public static void checkEquals(final List<ReplicatedPersonEntity> expected,
                                   final List<ReplicatedPersonEntity> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }
}
