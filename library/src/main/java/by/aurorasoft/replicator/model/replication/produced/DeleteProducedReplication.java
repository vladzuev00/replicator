package by.aurorasoft.replicator.model.replication.produced;

public final class DeleteProducedReplication extends ProducedReplication {

    public DeleteProducedReplication(Object entityId) {
        super(entityId);
    }

    @Override
    protected Object getEntityId(Object entityId) {
        return entityId;
    }
}
