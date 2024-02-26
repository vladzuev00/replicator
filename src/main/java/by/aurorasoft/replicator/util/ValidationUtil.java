package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ValidationUtil {

    public static void withinIntervalOrElseThrow(final int value, final int min, final int max, final String propertyName) {
        if (!isWithinInterval(value, min, max)) {
            throw new IllegalArgumentException(
                    "Not valid '%s'. It should be within [%d, %d], but was %d".formatted(propertyName, min, max, value)
            );
        }
    }

    private static boolean isWithinInterval(final int value, final int min, final int max) {
        return min <= value && value <= max;
    }
}
