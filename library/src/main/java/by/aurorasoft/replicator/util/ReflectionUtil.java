package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.ReflectionUtils.findField;

@UtilityClass
public final class ReflectionUtil {
    private static final String NO_FIELD_MESSAGE_TEMPLATE = "There is no field '%s' in '%s'";

    public static <V> V getFieldValue(Object object, String fieldName, Class<V> type) {
        Field field = getField(object, fieldName);
        field.setAccessible(true);
        try {
            Object value = getFieldValue(object, field);
            return type.cast(value);
        } finally {
            field.setAccessible(false);
        }
    }

    private static Field getField(Object object, String fieldName) {
        return requireNonNull(
                findField(object.getClass(), fieldName),
                () -> NO_FIELD_MESSAGE_TEMPLATE.formatted(fieldName, object.getClass())
        );
    }

    private static Object getFieldValue(Object object, Field field) {
        return ReflectionUtils.getField(field, object);
    }
}
