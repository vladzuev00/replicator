package by.aurorasoft.replicator.callback;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionSynchronization;

@RequiredArgsConstructor
public abstract class ProduceReplicationCallback implements TransactionSynchronization {
    private final ReplicationProducer producer;
    private final Object source;

    @Override
    public final void afterCommit() {
        ProducedReplication<?> replication = createReplication(source);
        producer.send(replication);
    }

    protected abstract ProducedReplication<?> createReplication(Object source);
}
