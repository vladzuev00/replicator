package by.aurorasoft.replicator.callback;

import by.aurorasoft.replicator.producer.ReplicationProducer;

public final class SaveReplicationCallback extends ReplicationCallback {

    public SaveReplicationCallback(Object savedEntity, ReplicationProducer producer) {
        super(savedEntity, producer);
    }

    @Override
    protected void produce(Object savedEntity, ReplicationProducer producer) {
        producer.produceSave(savedEntity);
    }
}
