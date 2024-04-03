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

//TODO: refactor with tests
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
            if (isPerhapsRelationNotDeliveredYet(exception)) {
                throw new PerhapsRelationNotDeliveredYetException(exception);
            } else {
                throw new RuntimeException(exception);
            }
        }
    }

    private static boolean isPerhapsRelationNotDeliveredYet(final Throwable exception) {
        return (getRootCause(exception) instanceof final SQLException sqlException) && isInvalidForeignKey(sqlException);
    }

    private static boolean isInvalidForeignKey(final SQLException exception) {
        return Objects.equals(INVALID_FOREIGN_KEY_SQL_STATE, exception.getSQLState());
    }
}
