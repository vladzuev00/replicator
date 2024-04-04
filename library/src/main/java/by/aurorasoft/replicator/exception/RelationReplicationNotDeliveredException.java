package by.aurorasoft.replicator.exception;

public final class RelationReplicationNotDeliveredException extends RuntimeException {

    @SuppressWarnings("unused")
    public RelationReplicationNotDeliveredException() {

    }

    @SuppressWarnings("unused")
    public RelationReplicationNotDeliveredException(final String description) {
        super(description);
    }

    public RelationReplicationNotDeliveredException(final Exception cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public RelationReplicationNotDeliveredException(final String description, final Exception cause) {
        super(description, cause);
    }
}
