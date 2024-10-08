package by.aurorasoft.replicator.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.PropertyDescriptor;

import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
import static java.util.Objects.requireNonNull;
import static org.springframework.beans.BeanUtils.getPropertyDescriptor;

@UtilityClass
public final class PropertyUtil {
    static final String FIELD_NAME_ID = "id";
    static final String FIELD_NAME_REPOSITORY = "repository";

    @SneakyThrows
    public static Object getId(Object object) {
        return getIdDescriptor(object.getClass())
                .getReadMethod()
                .invoke(object);
    }

    public static JpaRepository<?, ?> getJpaRepository(Object object) {
        return getFieldValue(object, FIELD_NAME_REPOSITORY, JpaRepository.class);
    }

    private static PropertyDescriptor getIdDescriptor(Class<?> type) {
        PropertyDescriptor descriptor = getPropertyDescriptor(type, FIELD_NAME_ID);
        return requireNonNull(descriptor, () -> "There is no id's getter in %s".formatted(type));
    }
}
