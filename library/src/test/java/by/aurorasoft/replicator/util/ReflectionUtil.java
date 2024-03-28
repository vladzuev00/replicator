package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.ReflectionUtils.findField;

@UtilityClass
public final class ReflectionUtil {

    public static <V, T> V getFieldValue(final T target, final String fieldName, final Class<V> valueType) {
        final Field field = getField(target, fieldName);
        field.setAccessible(true);
        try {
            final Object value = getFieldValue(field, target);
            return valueType.cast(value);
        } finally {
            field.setAccessible(false);
        }
    }

    private static Field getField(final Object target, final String fieldName) {
        return requireNonNull(findField(target.getClass(), fieldName));
    }

    private static Object getFieldValue(final Field field, final Object target) {
        return ReflectionUtils.getField(field, target);
    }
}
