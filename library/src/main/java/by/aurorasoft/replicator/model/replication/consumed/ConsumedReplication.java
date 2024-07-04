package by.aurorasoft.replicator.model.replication.consumed;

import by.aurorasoft.replicator.exception.RelatedReplicationNotDeliveredException;
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

    public final void execute(JpaRepository<E, ID> repository) {
        try {
            executeInternal(repository);
        } catch (Exception exception) {
            throw wrapInRuntimeException(exception);
        }
    }

    protected abstract void executeInternal(JpaRepository<E, ID> repository);

    private RuntimeException wrapInRuntimeException(Exception exception) {
        return isRelatedReplicationNotDelivered(exception)
                ? new RelatedReplicationNotDeliveredException(exception)
                : new ReplicationExecutionException(exception);
    }

    private boolean isRelatedReplicationNotDelivered(Throwable exception) {
        return (getRootCause(exception) instanceof SQLException cause) && isForeignKeyViolation(cause);
    }

    private boolean isForeignKeyViolation(SQLException exception) {
        return Objects.equals(FOREIGN_KEY_VIOLATION_SQL_STATE, exception.getSQLState());
    }

    static final class ReplicationExecutionException extends RuntimeException {

        public ReplicationExecutionException(Exception cause) {
            super(cause);
        }
    }
}
