package by.aurorasoft.replicator.model.replication.produced;

public final class DeleteProducedReplication extends ProducedReplication<Object> {

    public DeleteProducedReplication(Object entityId) {
        super(entityId);
    }

    @Override
    protected Object getEntityIdInternal(Object entityId) {
        return entityId;
    }
}
