package by.aurorasoft.replicator.model.replication.consumed;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.exception.RelatedReplicationNotDeliveredException;
import by.aurorasoft.replicator.mapperwrapper.ReplicationObjectMapperWrapper;
import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication.ReplicationExecutionException;
import by.aurorasoft.replicator.testentity.TestEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication.FOREIGN_KEY_VIOLATION_SQL_STATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public final class ConsumedReplicationTest extends AbstractSpringBootTest {
    private static final String GIVEN_SQL_EXCEPTION_REASON = "reason";
    private static final String UNIQUE_VIOLATION_SQL_STATE = "23505";

    @Autowired
    private ReplicationObjectMapperWrapper objectMapperWrapper;

    @Test
    public void replicationShouldBeExecuted() {
        TestEntity givenEntity = TestEntity.builder().build();
        TestConsumedReplication givenReplication = new TestConsumedReplication(givenEntity);
        @SuppressWarnings("unchecked") JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);

        givenReplication.execute(givenRepository);

        verify(givenRepository, times(1)).save(same(givenEntity));
    }

    @ParameterizedTest
    @MethodSource("provideJsonAndExpectedReplication")
    public void replicationShouldBeDeserializedFromJson(String givenJson,
                                                        ConsumedReplication<TestEntity, Long> expected) {
        ConsumedReplication<TestEntity, Long> actual = deserialize(givenJson);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideCauseAndExpectedExceptionType")
    public void replicationShouldNotBeExecuted(Exception givenCause, Class<? extends Exception> expected) {
        TestEntity givenEntity = TestEntity.builder().build();
        TestConsumedReplication givenReplication = new TestConsumedReplication(givenEntity);
        @SuppressWarnings("unchecked") JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);

        when(givenRepository.save(same(givenEntity))).thenThrow(givenCause);

        executeExpectingException(givenReplication, givenRepository, expected);
    }

    @SneakyThrows(JsonProcessingException.class)
    private ConsumedReplication<TestEntity, Long> deserialize(String json) {
        return objectMapperWrapper.getMapper().readValue(
                json,
                new TypeReference<>() {
                }
        );
    }

    private void executeExpectingException(TestConsumedReplication replication,
                                           JpaRepository<TestEntity, Long> repository,
                                           Class<? extends Exception> exceptionType) {
        boolean exceptionArisen;
        try {
            replication.execute(repository);
            exceptionArisen = false;
        } catch (Exception exception) {
            assertTrue(exceptionType.isInstance(exception));
            exceptionArisen = true;
        }
        assertTrue(exceptionArisen);
    }

    private static Stream<Arguments> provideJsonAndExpectedReplication() {
        return Stream.of(
                Arguments.of(
                        """
                                {
                                   "type": "save",
                                   "body": {
                                     "id": 255,
                                     "firstProperty": "first-value",
                                     "secondProperty": "second-value"
                                   }
                                }""",
                        new SaveConsumedReplication<>(new TestEntity(255L, "first-value", "second-value"))
                ),
                Arguments.of(
                        """
                                {
                                  "type": "delete",
                                  "body": 255
                                }""",
                        new DeleteConsumedReplication<>(255L)
                )
        );
    }

    private static Stream<Arguments> provideCauseAndExpectedExceptionType() {
        return Stream.of(
                Arguments.of(
                        createSqlExceptionWrappingByRuntime(FOREIGN_KEY_VIOLATION_SQL_STATE),
                        RelatedReplicationNotDeliveredException.class
                ),
                Arguments.of(
                        createSqlExceptionWrappingByRuntime(UNIQUE_VIOLATION_SQL_STATE),
                        ReplicationExecutionException.class
                ),
                Arguments.of(
                        new RuntimeException(),
                        ReplicationExecutionException.class
                )
        );
    }

    private static RuntimeException createSqlExceptionWrappingByRuntime(String sqlState) {
        return new RuntimeException(new SQLException(GIVEN_SQL_EXCEPTION_REASON, sqlState));
    }

    @RequiredArgsConstructor
    private static final class TestConsumedReplication extends ConsumedReplication<TestEntity, Long> {
        private final TestEntity entity;

        @Override
        protected void executeInternal(JpaRepository<TestEntity, Long> repository) {
            repository.save(entity);
        }
    }
}
