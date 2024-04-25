package by.aurorasoft.replicator.model.replication.produced;

import java.lang.reflect.InvocationTargetException;

import static java.util.Objects.requireNonNull;
import static org.springframework.beans.BeanUtils.getPropertyDescriptor;

public final class SaveProducedReplication extends ProducedReplication {
    private static final String FIELD_NAME_ID = "id";

    public SaveProducedReplication(final Object entity) {
        super(entity);
    }

    @Override
    protected Object getEntityId(final Object entity) {
        try {
            return requireNonNull(getPropertyDescriptor(entity.getClass(), FIELD_NAME_ID))
                    .getReadMethod()
                    .invoke(entity);
        } catch (final IllegalAccessException | InvocationTargetException cause) {
            throw new EntityIdExtractionException(cause);
        }
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
