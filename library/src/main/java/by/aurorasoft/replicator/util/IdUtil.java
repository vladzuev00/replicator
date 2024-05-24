package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;

@UtilityClass
public final class IdUtil {
    private static final String FIELD_NAME_ID = "id";

    public static Object getId(final Object object) {
        try {
            return getIdDescriptor(object)
                    .getReadMethod()
                    .invoke(object);
        } catch (final IllegalAccessException | InvocationTargetException cause) {
            throw new GettingIdException(cause);
        }
    }

    private PropertyDescriptor getIdDescriptor(final Object object) {
        return getPropertyDescriptor(object.getClass(), FIELD_NAME_ID);
    }

    static final class GettingIdException extends RuntimeException {

        @SuppressWarnings("unused")
        public GettingIdException() {

        }

        @SuppressWarnings("unused")
        public GettingIdException(final String description) {
            super(description);
        }

        public GettingIdException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public GettingIdException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
