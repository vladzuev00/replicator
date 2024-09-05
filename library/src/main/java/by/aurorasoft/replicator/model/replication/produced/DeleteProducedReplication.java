package by.aurorasoft.replicator.model.replication.produced;

public final class DeleteProducedReplication extends ProducedReplication<Object> {

    public DeleteProducedReplication(Object dtoId) {
        super(dtoId);
    }

    @Override
    protected Object getDtoIdInternal(Object dtoId) {
        return dtoId;
    }
}
