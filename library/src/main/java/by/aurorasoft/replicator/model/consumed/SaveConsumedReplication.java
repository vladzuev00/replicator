package by.aurorasoft.replicator.model.consumed;

import by.aurorasoft.replicator.exception.PerhapsRelationNotDeliveredYetException;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.Objects;

import static by.aurorasoft.replicator.util.TransportNameUtil.BODY_NAME;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;

@Getter
@EqualsAndHashCode
@ToString
public final class SaveConsumedReplication<ID, E extends AbstractEntity<ID>> implements ConsumedReplication<ID, E> {
    private static final String INVALID_FOREIGN_KEY_SQL_STATE = "23503";

    private final E entity;

    @JsonCreator
    public SaveConsumedReplication(@JsonProperty(BODY_NAME) final E entity) {
        this.entity = entity;
    }

    @Override
    public void execute(final JpaRepository<E, ID> repository) {
        try {
            repository.save(entity);
        } catch (final Exception exception) {
            throw wrapFailedExecutionException(exception);
        }
    }

    private static RuntimeException wrapFailedExecutionException(final Exception cause) {
        return isPerhapsRelationNotDeliveredYet(cause)
                ? new PerhapsRelationNotDeliveredYetException(cause)
                : new SaveReplicationExecutionException(cause);
    }

    private static boolean isPerhapsRelationNotDeliveredYet(final Exception exception) {
        return (getRootCause(exception) instanceof final SQLException sqlException) && isInvalidForeignKey(sqlException);
    }

    private static boolean isInvalidForeignKey(final SQLException exception) {
        return Objects.equals(INVALID_FOREIGN_KEY_SQL_STATE, exception.getSQLState());
    }

    static final class SaveReplicationExecutionException extends RuntimeException {

        @SuppressWarnings("unused")
        public SaveReplicationExecutionException() {

        }

        @SuppressWarnings("unused")
        public SaveReplicationExecutionException(final String description) {
            super(description);
        }

        public SaveReplicationExecutionException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public SaveReplicationExecutionException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
