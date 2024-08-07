package by.aurorasoft.replicator.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.beans.PropertyDescriptor;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;

@UtilityClass
public final class IdUtil {
    private static final String FIELD_NAME_ID = "id";

    @SneakyThrows
    public static Object getId(Object object) {
        return getIdDescriptor(object)
                .getReadMethod()
                .invoke(object);
    }

    private PropertyDescriptor getIdDescriptor(Object object) {
        PropertyDescriptor descriptor = getPropertyDescriptor(object.getClass(), FIELD_NAME_ID);
        if (descriptor == null) {
            throw new NoIdGetterException("There is no id's getter in '%s'".formatted(object));
        }
        return descriptor;
    }

    static final class NoIdGetterException extends RuntimeException {

        public NoIdGetterException(String description) {
            super(description);
        }
    }
}
