package by.aurorasoft.replicator.transactioncallback;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionSynchronization;

@RequiredArgsConstructor
@Getter
public abstract class ReplicationTransactionCallback implements TransactionSynchronization {
    private final Object body;
    private final ReplicationProducer producer;

    @Override
    public final void afterCommit() {
        produce(body, producer);
    }

    protected abstract void produce(Object body, ReplicationProducer producer);
}
