package by.aurorasoft.testapp.testutil;

import by.aurorasoft.testapp.crud.entity.AbstractEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.BiConsumer;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;

@UtilityClass
public final class EntityUtil {

    public static <E extends AbstractEntity> void checkEquals(final List<E> expected,
                                                              final List<E> actual,
                                                              final BiConsumer<E, E> equalChecker) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> equalChecker.accept(expected.get(i), actual.get(i)));
    }
}
