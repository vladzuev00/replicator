package by.aurorasoft.replicator.model.consumed;

import by.aurorasoft.replicator.exception.PerhapsRelationNotDeliveredYetException;
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
    private static final String INVALID_FOREIGN_KEY_SQL_STATE = "23503";

    public final void execute(final JpaRepository<E, ID> repository) {
        try {
            executeInternal(repository);
        } catch (final Exception exception) {
            handleFailedExecution(exception);
        }
    }

    protected abstract void executeInternal(final JpaRepository<E, ID> repository);

    private static void handleFailedExecution(final Exception exception) {
        if (isPerhapsRelationNotDeliveredYet(exception)) {
            throw new PerhapsRelationNotDeliveredYetException(exception);
        }
    }

    private static boolean isPerhapsRelationNotDeliveredYet(final Exception exception) {
        return (getRootCause(exception) instanceof final SQLException sqlException) && isInvalidForeignKey(sqlException);
    }

    private static boolean isInvalidForeignKey(final SQLException exception) {
        return Objects.equals(INVALID_FOREIGN_KEY_SQL_STATE, exception.getSQLState());
    }
}
