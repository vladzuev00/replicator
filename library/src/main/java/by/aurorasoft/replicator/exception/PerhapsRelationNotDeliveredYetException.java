package by.aurorasoft.replicator.exception;

public final class PerhapsRelationNotDeliveredYetException extends RuntimeException {

    public PerhapsRelationNotDeliveredYetException() {

    }

    public PerhapsRelationNotDeliveredYetException(final String description) {
        super(description);
    }

    public PerhapsRelationNotDeliveredYetException(final Exception cause) {
        super(cause);
    }

    public PerhapsRelationNotDeliveredYetException(final String description, final Exception cause) {
        super(description, cause);
    }
}
