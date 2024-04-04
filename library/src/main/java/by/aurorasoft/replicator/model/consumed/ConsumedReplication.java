package by.aurorasoft.replicator.model.consumed;

import by.aurorasoft.replicator.exception.RelationReplicationNotDeliveredException;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.Objects;

import static by.aurorasoft.replicator.util.TransportNameUtil.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;

@JsonTypeInfo(use = NAME, property = TYPE_PROPERTY)
@JsonSubTypes(
        {
                @Type(value = SaveConsumedReplication.class, name = SAVE),
                @Type(value = DeleteConsumedReplication.class, name = DELETE)
        }
)
public abstract class ConsumedReplication<ID, E extends AbstractEntity<ID>> {
    private static final String FOREIGN_KEY_VIOLATION_SQL_STATE = "23503";

    public final void execute(final JpaRepository<E, ID> repository) {
        try {
            executeInternal(repository);
        } catch (final Exception exception) {
            throw wrapToRuntimeExecutionException(exception);
        }
    }

    protected abstract void executeInternal(final JpaRepository<E, ID> repository);

    private static RuntimeException wrapToRuntimeExecutionException(final Exception exception) {
        return isRelationReplicationNotDelivered(exception)
                ? new RelationReplicationNotDeliveredException(exception)
                : new ReplicationConsumingException(exception);
    }

    private static boolean isRelationReplicationNotDelivered(final Throwable exception) {
        return (getRootCause(exception) instanceof final SQLException rootCause) && isForeignKeyViolation(rootCause);
    }

    private static boolean isForeignKeyViolation(final SQLException exception) {
        return Objects.equals(FOREIGN_KEY_VIOLATION_SQL_STATE, exception.getSQLState());
    }

    static final class ReplicationConsumingException extends RuntimeException {

        @SuppressWarnings("unused")
        public ReplicationConsumingException() {

        }

        @SuppressWarnings("unused")
        public ReplicationConsumingException(final String description) {
            super(description);
        }

        public ReplicationConsumingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ReplicationConsumingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
