package by.aurorasoft.replicator.transactioncallback;

import by.aurorasoft.replicator.producer.ReplicationProducer;

public final class SaveReplicationTransactionCallback extends ReplicationTransactionCallback {

    public SaveReplicationTransactionCallback(Object savedEntity, ReplicationProducer producer) {
        super(savedEntity, producer);
    }

    @Override
    protected void produce(Object savedEntity, ReplicationProducer producer) {
        producer.produceSaveAfterCommit(savedEntity);
    }
}
