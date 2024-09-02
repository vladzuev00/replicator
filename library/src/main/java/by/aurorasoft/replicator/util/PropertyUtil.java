package by.aurorasoft.replicator.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.TypesUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.ReflectionUtils;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.beans.PropertyDescriptor;

import static java.util.Objects.requireNonNull;
import static org.checkerframework.javacutil.ElementUtils.findFieldInType;
import static org.springframework.beans.BeanUtils.getPropertyDescriptor;

@UtilityClass
public final class PropertyUtil {
    private static final String FIELD_NAME_ID = "id";

    public static boolean isContainId(TypeMirror type) {
        return findFieldInType(TypesUtils.getTypeElement(type), FIELD_NAME_ID) != null;
    }

    @SneakyThrows
    public static Object getId(Object object) {
        return getIdDescriptor(object.getClass())
                .getReadMethod()
                .invoke(object);
    }

    public static JpaRepository<?, ?> getJpaRepository(Object object) {
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
