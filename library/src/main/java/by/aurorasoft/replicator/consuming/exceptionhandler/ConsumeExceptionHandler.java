package by.aurorasoft.replicator.consuming.exceptionhandler;

import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Objects;

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD;
import static org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;

@Component
public final class ConsumeExceptionHandler implements StreamsUncaughtExceptionHandler {
    private static final String INVALID_FOREIGN_KEY_SQL_STATE = "23503";

    @Override
    public StreamThreadExceptionResponse handle(final Throwable exception) {
        return isPerhapsRelationNotDeliveredYet(exception) ? REPLACE_THREAD : SHUTDOWN_APPLICATION;
    }

    private static boolean isPerhapsRelationNotDeliveredYet(final Throwable exception) {
        return (getRootCause(exception) instanceof final SQLException sqlException) && isInvalidForeignKey(sqlException);
    }

    private static boolean isInvalidForeignKey(final SQLException exception) {
        return Objects.equals(INVALID_FOREIGN_KEY_SQL_STATE, exception.getSQLState());
    }
}
