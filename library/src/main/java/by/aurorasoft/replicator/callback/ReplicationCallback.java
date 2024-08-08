package by.aurorasoft.replicator.callback;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionSynchronization;

@RequiredArgsConstructor
public abstract class ReplicationCallback implements TransactionSynchronization {
    private final Object body;
    private final ReplicationProducer producer;

    @Override
    public final void afterCommit() {
        produce(body, producer);
    }

    protected abstract void produce(Object body, ReplicationProducer producer);
}
