package by.aurorasoft.replicator.transaction.manager;

import by.aurorasoft.replicator.transaction.callback.ReplicationTransactionCallback;
import org.springframework.stereotype.Component;

import static org.springframework.transaction.support.TransactionSynchronizationManager.isActualTransactionActive;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

@Component
public final class ReplicationTransactionManager {

    public void register(ReplicationTransactionCallback callback) {
        if (isActualTransactionActive()) {
            registerSynchronization(callback);
        } else {
            callback.afterCommit();
        }
    }
}
