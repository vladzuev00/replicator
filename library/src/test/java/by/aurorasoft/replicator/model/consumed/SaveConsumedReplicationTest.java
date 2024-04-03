package by.aurorasoft.replicator.model.consumed;

import by.aurorasoft.replicator.base.entity.TestEntity;
import by.aurorasoft.replicator.exception.PerhapsRelationNotDeliveredYetException;
import by.aurorasoft.replicator.model.consumed.SaveConsumedReplication.SaveReplicationExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class SaveConsumedReplicationTest {
    private static final String GIVEN_SQL_EXCEPTION_REASON = "reason";

    private static final String INVALID_FOREIGN_KEY_SQL_STATE = "23503";
    private static final String UNIQUE_VIOLATION_SQL_STATE = "23505";

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeExecuted() {
        final TestEntity givenEntity = new TestEntity(255L);
        final SaveConsumedReplication<Long, TestEntity> givenReplication = new SaveConsumedReplication<>(givenEntity);

        final JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);

        givenReplication.execute(givenRepository);

        verify(givenRepository, times(1)).save(same(givenEntity));
    }

    @ParameterizedTest
    @SuppressWarnings("unchecked")
    @MethodSource("provideCauseAndExpectedExceptionType")
    public void replicationShouldNotBeExecuted(final Exception givenCause, final Class<? extends Exception> expected) {
        final TestEntity givenEntity = new TestEntity(255L);
        final SaveConsumedReplication<Long, TestEntity> givenReplication = new SaveConsumedReplication<>(givenEntity);

        final JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);

        when(givenRepository.save(same(givenEntity))).thenThrow(givenCause);

        executeExpectingException(givenReplication, givenRepository, expected);
    }

    private static Stream<Arguments> provideCauseAndExpectedExceptionType() {
        return Stream.of(
                Arguments.of(createSqlException(INVALID_FOREIGN_KEY_SQL_STATE), PerhapsRelationNotDeliveredYetException.class),
                Arguments.of(createSqlException(UNIQUE_VIOLATION_SQL_STATE), SaveReplicationExecutionException.class),
                Arguments.of(new RuntimeException(), SaveReplicationExecutionException.class)
        );
    }

    private static RuntimeException createSqlException(final String sqlState) {
        return new RuntimeException(new SQLException(GIVEN_SQL_EXCEPTION_REASON, sqlState));
    }

    private void executeExpectingException(final SaveConsumedReplication<Long, TestEntity> replication,
                                           final JpaRepository<TestEntity, Long> repository,
                                           final Class<? extends Exception> exceptionType) {
        boolean exceptionArisen;
        try {
            replication.execute(repository);
            exceptionArisen = false;
        } catch (final Exception exception) {
            assertTrue(exceptionType.isInstance(exception));
            exceptionArisen = true;
        }
        assertTrue(exceptionArisen);
    }
}
