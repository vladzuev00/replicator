package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.getField;

@UtilityClass
public final class ReflectionUtil {

    public static <V, T> V getFieldValue(final T target, final String fieldName, final Class<V> valueType) {
        final Field field = requireNonNull(findField(target.getClass(), fieldName));
        field.setAccessible(true);
        try {
            final Object value = getField(field, target);
            return valueType.cast(value);
        } finally {
            field.setAccessible(false);
        }
    }
}
