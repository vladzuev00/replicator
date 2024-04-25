package by.aurorasoft.replicator.model.replication.consumed;

import by.aurorasoft.replicator.exception.RelationReplicationNotDeliveredException;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.Objects;

import static by.aurorasoft.replicator.util.TransportNameUtil.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;

@JsonTypeInfo(use = NAME, property = TYPE)
@JsonSubTypes(
        {
                @Type(value = SaveConsumedReplication.class, name = SAVE),
                @Type(value = DeleteConsumedReplication.class, name = DELETE)
        }
)
@EqualsAndHashCode
@ToString
public abstract class ConsumedReplication<E, ID> {
    static final String FOREIGN_KEY_VIOLATION_SQL_STATE = "23503";

    public final void execute(final JpaRepository<E, ID> repository) {
        try {
            executeInternal(repository);
        } catch (final Exception exception) {
            throw wrapToExecutionException(exception);
        }
    }

    protected abstract void executeInternal(final JpaRepository<E, ID> repository);

    private static RuntimeException wrapToExecutionException(final Exception exception) {
        return isRelationReplicationNotDelivered(exception)
                ? new RelationReplicationNotDeliveredException(exception)
                : new ReplicationExecutionException(exception);
    }

    private static boolean isRelationReplicationNotDelivered(final Throwable exception) {
        return (getRootCause(exception) instanceof final SQLException cause) && isForeignKeyViolation(cause);
    }

    private static boolean isForeignKeyViolation(final SQLException exception) {
        return Objects.equals(FOREIGN_KEY_VIOLATION_SQL_STATE, exception.getSQLState());
    }

    static final class ReplicationExecutionException extends RuntimeException {

        @SuppressWarnings("unused")
        public ReplicationExecutionException() {

        }

        @SuppressWarnings("unused")
        public ReplicationExecutionException(final String description) {
            super(description);
        }

        public ReplicationExecutionException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ReplicationExecutionException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
