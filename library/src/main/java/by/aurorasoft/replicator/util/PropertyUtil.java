package by.aurorasoft.replicator.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.TypesUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.ReflectionUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.checkerframework.javacutil.ElementUtils.findFieldInType;
import static org.checkerframework.javacutil.TypesUtils.getClassFromType;
import static org.checkerframework.javacutil.TypesUtils.getTypeElement;
import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.util.ReflectionUtils.findField;

@UtilityClass
public final class PropertyUtil {
    private static final String FIELD_NAME_ID = "id";

    @SneakyThrows
    public static Object getId(Object object) {
        return getIdDescriptor(object.getClass())
                .getReadMethod()
                .invoke(object);
    }

    public static JpaRepository<?, ?> getJpaRepository(Object object) {
        Field field = requireNonNull(findField(object.getClass(), "repository"));
        field.setAccessible(true);
        try {
            return (JpaRepository<?, ?>) org.springframework.util.ReflectionUtils.getField(field, object);
        } finally {
            field.setAccessible(false);
        }
    }

    private PropertyDescriptor getIdDescriptor(Class<?> type) {
        PropertyDescriptor descriptor = getPropertyDescriptor(type, FIELD_NAME_ID);
        return requireNonNull(descriptor, "There is no id's getter in %s".formatted(type));
    }
}
