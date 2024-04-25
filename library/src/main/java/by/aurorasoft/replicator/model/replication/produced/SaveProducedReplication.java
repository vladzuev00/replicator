package by.aurorasoft.replicator.model.replication.produced;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;

public final class SaveProducedReplication extends ProducedReplication {
    private static final String FIELD_NAME_ID = "id";

    public SaveProducedReplication(final Object dto) {
        super(dto);
    }

    @Override
    protected Object getEntityId(final Object dto) {
        try {
            return getIdDescriptor(dto)
                    .getReadMethod()
                    .invoke(dto);
        } catch (final IllegalAccessException | InvocationTargetException cause) {
            throw new EntityIdExtractionException(cause);
        }
    }

    private PropertyDescriptor getIdDescriptor(final Object body) {
        return getPropertyDescriptor(body.getClass(), FIELD_NAME_ID);
    }

    static final class EntityIdExtractionException extends RuntimeException {

        @SuppressWarnings("unused")
        public EntityIdExtractionException() {

        }

        @SuppressWarnings("unused")
        public EntityIdExtractionException(final String description) {
            super(description);
        }

        public EntityIdExtractionException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public EntityIdExtractionException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
