package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.ReflectionUtils.findField;

@UtilityClass
public final class ReflectionUtil {

    public static <V, T> V getFieldValue(T target, String fieldName, Class<V> valueType) {
        Field field = requireNonNull(findField(target.getClass(), fieldName));
        field.setAccessible(true);
        try {
            Object value = ReflectionUtils.getField(field, target);
            return valueType.cast(value);
        } finally {
            field.setAccessible(false);
        }
    }

    public static <V, T> V getFieldValue(T target, String fieldName, Class<V> valueType, Supplier<String> noFieldMessageSupplier) {
        Field field = requireNonNull(findField(target.getClass(), fieldName), noFieldMessageSupplier);
        field.setAccessible(true);
        try {
            Object value = ReflectionUtils.getField(field, target);
            return valueType.cast(value);
        } finally {
            field.setAccessible(false);
        }
    }
}
