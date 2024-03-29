package by.aurorasoft.replicator.consuming.exceptionhandler;

import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD;
import static org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ReplicationConsumeExceptionHandlerTest {
    private static final String GIVEN_SQL_EXCEPTION_REASON = "reason";

    private static final String INVALID_FOREIGN_KEY_SQL_STATE = "23503";
    private static final String UNIQUE_VIOLATION_SQL_STATE = "23505";

    private final ReplicationConsumeExceptionHandler handler = new ReplicationConsumeExceptionHandler();

    @ParameterizedTest
    @MethodSource("provideExceptionAndExpectedResponse")
    public void exceptionShouldBeHandled(final Throwable givenException, final StreamThreadExceptionResponse expected) {
        final StreamThreadExceptionResponse actual = handler.handle(givenException);
        assertSame(expected, actual);
    }

    private static Stream<Arguments> provideExceptionAndExpectedResponse() {
        return Stream.of(
                Arguments.of(createExceptionCausedByForeignKeySqlException(), REPLACE_THREAD),
                Arguments.of(createExceptionCausedByUniqueViolationSqlException(), SHUTDOWN_APPLICATION),
                Arguments.of(createInvalidForeignKeySqlException(), REPLACE_THREAD),
                Arguments.of(createUniqueViolationSqlException(), SHUTDOWN_APPLICATION),
                Arguments.of(new RuntimeException(), SHUTDOWN_APPLICATION)
        );
    }

    private static RuntimeException createExceptionCausedByForeignKeySqlException() {
        return createExceptionCausedBySqlException(
                ReplicationConsumeExceptionHandlerTest::createInvalidForeignKeySqlException
        );
    }

    private static RuntimeException createExceptionCausedByUniqueViolationSqlException() {
        return createExceptionCausedBySqlException(
                ReplicationConsumeExceptionHandlerTest::createUniqueViolationSqlException
        );
    }

    private static RuntimeException createExceptionCausedBySqlException(final Supplier<SQLException> causeSupplier) {
        final SQLException cause = causeSupplier.get();
        return new RuntimeException(cause);
    }

    private static SQLException createInvalidForeignKeySqlException() {
        return createSqlException(INVALID_FOREIGN_KEY_SQL_STATE);
    }

    private static SQLException createUniqueViolationSqlException() {
        return createSqlException(UNIQUE_VIOLATION_SQL_STATE);
    }

    private static SQLException createSqlException(final String sqlState) {
        return new SQLException(GIVEN_SQL_EXCEPTION_REASON, sqlState);
    }
}
