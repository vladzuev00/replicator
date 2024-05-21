package by.aurorasoft.replicator.exception;

public final class RelatedReplicationNotDeliveredException extends RuntimeException {

    @SuppressWarnings("unused")
    public RelatedReplicationNotDeliveredException() {

    }

    @SuppressWarnings("unused")
    public RelatedReplicationNotDeliveredException(final String description) {
        super(description);
    }

    public RelatedReplicationNotDeliveredException(final Exception cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public RelatedReplicationNotDeliveredException(final String description, final Exception cause) {
        super(description, cause);
    }
}
