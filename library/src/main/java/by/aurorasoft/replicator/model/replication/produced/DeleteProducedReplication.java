package by.aurorasoft.replicator.model.replication.produced;

public final class DeleteProducedReplication extends ProducedReplication {

    public DeleteProducedReplication(final Object body) {
        super(body);
    }

    @Override
    protected Object getEntityId(final Object body) {
        return body;
    }
}
