package by.aurorasoft.replicator.model.replication.produced;

public final class DeleteProducedReplication extends ProducedReplication {

    public DeleteProducedReplication(final Object entityId) {
        super(entityId);
    }

    @Override
    protected Object getEntityId(final Object entityId) {
        return entityId;
    }
}
