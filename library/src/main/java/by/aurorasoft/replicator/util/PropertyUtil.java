package by.aurorasoft.replicator.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import static java.util.Objects.requireNonNull;
import static org.springframework.beans.BeanUtils.getPropertyDescriptor;

@UtilityClass
public final class PropertyUtil {
    private static final String FIELD_NAME_ID = "id";

    @SneakyThrows
    public static Object getId(Object object) {
        return getIdDescriptor(object.getClass())
                .getReadMethod()
                .invoke(object);
    }

    public static JpaRepository<?, ?> getRepository(Object object) {
        try {
            return (JpaRepository<?, ?>) ReflectionUtils.findField(object.getClass(), field -> field.getType() == JpaRepository.class).get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private PropertyDescriptor getIdDescriptor(Class<?> type) {
        PropertyDescriptor descriptor = getPropertyDescriptor(type, FIELD_NAME_ID);
        return requireNonNull(descriptor, "There is no id's getter in %s".formatted(type));
    }
}
