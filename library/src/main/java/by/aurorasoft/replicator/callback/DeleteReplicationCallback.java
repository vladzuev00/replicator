package by.aurorasoft.replicator.callback;

import by.aurorasoft.replicator.producer.ReplicationProducer;

public final class DeleteReplicationCallback extends ReplicationCallback {

    public DeleteReplicationCallback(Object entityId, ReplicationProducer producer) {
        super(entityId, producer);
    }

    @Override
    protected void produce(Object entityId, ReplicationProducer producer) {
        producer.produceDelete(entityId);
    }
}
