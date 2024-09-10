package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.ReflectionUtils.findField;

@UtilityClass
public final class ReflectionUtil {

    public static <V, T> V getFieldValue(T target, String fieldName, Class<V> type) {
        Field field = requireNonNull(findField(target.getClass(), fieldName), "There is no field '%s' in '%s'".formatted(fieldName, target.getClass()));
        field.setAccessible(true);
        try {
            Object value = ReflectionUtils.getField(field, target);
            return type.cast(value);
        } finally {
            field.setAccessible(false);
        }
    }
}
