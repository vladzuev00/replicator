package by.aurorasoft.replicator.testutil;

import lombok.experimental.UtilityClass;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.ReflectionUtils.findField;

@UtilityClass
public final class ReflectionUtil {

    public static <V, T> V getFieldValue(T target, String fieldName, Class<V> valueType) {
        Field field = getField(target, fieldName);
        field.setAccessible(true);
        try {
            Object value = getFieldValue(field, target);
            return valueType.cast(value);
        } finally {
            field.setAccessible(false);
        }
    }

    private static Field getField(Object target, String fieldName) {
        return requireNonNull(findField(target.getClass(), fieldName));
    }

    private static Object getFieldValue(Field field, Object target) {
        return ReflectionUtils.getField(field, target);
    }
}
