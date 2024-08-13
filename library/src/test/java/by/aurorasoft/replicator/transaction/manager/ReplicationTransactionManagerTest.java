package by.aurorasoft.replicator.transaction.manager;

import by.aurorasoft.replicator.transaction.callback.ProduceReplicationTransactionCallback;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.mockito.Mockito.*;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

public final class ReplicationTransactionManagerTest {
    private final ReplicationTransactionManager manager = new ReplicationTransactionManager();

    @Test
    public void callbackShouldBeCalledAfterCommitInCaseTransactionIsActive() {
        try (var mockedTransactionSynchronizationManager = mockStatic(TransactionSynchronizationManager.class)) {
            ProduceReplicationTransactionCallback givenCallback = mock(ProduceReplicationTransactionCallback.class);

            mockedTransactionSynchronizationManager
                    .when(TransactionSynchronizationManager::isActualTransactionActive)
                    .thenReturn(true);

            manager.callAfterCommit(givenCallback);

            mockedTransactionSynchronizationManager.verify(() -> registerSynchronization(same(givenCallback)), times(1));
            verify(givenCallback, times(0)).afterCommit();
        }
    }

    @Test
    public void callbackShouldBeCalledAfterCommitInCaseTransactionIsNotActive() {
        try (var mockedTransactionSynchronizationManager = mockStatic(TransactionSynchronizationManager.class)) {
            ProduceReplicationTransactionCallback givenCallback = mock(ProduceReplicationTransactionCallback.class);

            mockedTransactionSynchronizationManager
                    .when(TransactionSynchronizationManager::isActualTransactionActive)
                    .thenReturn(false);

            manager.callAfterCommit(givenCallback);

            mockedTransactionSynchronizationManager.verify(() -> registerSynchronization(same(givenCallback)), times(0));
            verify(givenCallback, times(1)).afterCommit();
        }
    }
}
