package by.aurorasoft.replicator.exception;

public final class PerhapsRelationNotDeliveredYetException extends RuntimeException {

    @SuppressWarnings("unused")
    public PerhapsRelationNotDeliveredYetException() {

    }

    @SuppressWarnings("unused")
    public PerhapsRelationNotDeliveredYetException(final String description) {
        super(description);
    }

    public PerhapsRelationNotDeliveredYetException(final Exception cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public PerhapsRelationNotDeliveredYetException(final String description, final Exception cause) {
        super(description, cause);
    }
}
