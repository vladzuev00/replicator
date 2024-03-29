//package by.aurorasoft.replicator.model.consumed;
//
//import by.aurorasoft.replicator.base.entity.TestEntity;
//import by.aurorasoft.replicator.exception.PerhapsRelationNotDeliveredYetException;
//import by.aurorasoft.replicator.model.consumed.SaveConsumedReplication.SaveReplicationExecutionException;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.sql.SQLException;
//
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class SaveConsumedReplicationTest {
//    private static final String GIVEN_SQL_EXCEPTION_REASON = "reason";
//
//    @Mock
//    private JpaRepository<TestEntity, Long> mockedRepository;
//
//    @Test
//    public void replicationShouldBeExecuted() {
//        final TestEntity givenEntity = new TestEntity(255L);
//        final SaveConsumedReplication<Long, TestEntity> givenReplication = new SaveConsumedReplication<>(givenEntity);
//
//        execute(givenReplication);
//
//        verifySave(givenEntity);
//    }
//
//    @Test(expected = PerhapsRelationNotDeliveredYetException.class)
//    public void replicationShouldNotBeExecutedBecauseOfPerhapsRelationNotDeliveredYet() {
//        final TestEntity givenEntity = new TestEntity(255L);
//        final SaveConsumedReplication<Long, TestEntity> givenReplication = new SaveConsumedReplication<>(givenEntity);
//
//        executeWithSqlException(givenReplication, "23503");
//    }
//
//    @Test(expected = SaveReplicationExecutionException.class)
//    public void replicationShouldNotBeExecutedBecauseOfSQLException() {
//        final TestEntity givenEntity = new TestEntity(255L);
//        final SaveConsumedReplication<Long, TestEntity> givenReplication = new SaveConsumedReplication<>(givenEntity);
//
//        executeWithSqlException(givenReplication, "23502");
//    }
//
//    @Test(expected = SaveReplicationExecutionException.class)
//    public void replicationShouldNotBeExecutedBecauseOfSomeException() {
//        final TestEntity givenEntity = new TestEntity(255L);
//        final SaveConsumedReplication<Long, TestEntity> givenReplication = new SaveConsumedReplication<>(givenEntity);
//
//        final RuntimeException givenException = new RuntimeException("Something wrong");
//
//        executeWithException(givenReplication, givenException);
//    }
//
//    private void execute(final SaveConsumedReplication<Long, TestEntity> replication) {
//        replication.execute(mockedRepository);
//    }
//
//    private void executeWithException(final SaveConsumedReplication<Long, TestEntity> replication,
//                                      final RuntimeException exception) {
//        doThrow(exception).when(mockedRepository).save(same(replication.getEntity()));
//        execute(replication);
//    }
//
//    private void executeWithSqlException(final SaveConsumedReplication<Long, TestEntity> replication,
//                                         final String sqlState) {
//        executeWithException(replication, createExceptionCausedBySQLException(sqlState));
//    }
//
//    private static RuntimeException createExceptionCausedBySQLException(final String sqlState) {
//        final SQLException sqlException = new SQLException(GIVEN_SQL_EXCEPTION_REASON, sqlState);
//        return new RuntimeException(sqlException);
//    }
//
//    private void verifySave(final TestEntity entity) {
//        verify(mockedRepository, times(1)).save(same(entity));
//    }
//}
