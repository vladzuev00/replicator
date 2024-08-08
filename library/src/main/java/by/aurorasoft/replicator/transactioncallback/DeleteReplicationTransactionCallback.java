package by.aurorasoft.replicator.transactioncallback;

import by.aurorasoft.replicator.producer.ReplicationProducer;

public final class DeleteReplicationTransactionCallback extends ReplicationTransactionCallback {

    public DeleteReplicationTransactionCallback(Object entityId, ReplicationProducer producer) {
        super(entityId, producer);
    }

    @Override
    protected void produce(Object entityId, ReplicationProducer producer) {
        producer.produceDelete(entityId);
    }
}
