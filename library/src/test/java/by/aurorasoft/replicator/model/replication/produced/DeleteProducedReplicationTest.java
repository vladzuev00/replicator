package by.aurorasoft.replicator.model.replication.produced;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class DeleteProducedReplicationTest {

    @Test
    public void entityIdShouldBeGotInternally() {
        Long givenEntityId = 255L;
        DeleteProducedReplication givenReplication = new DeleteProducedReplication(null);

        Object actual = givenReplication.getEntityIdInternal(givenEntityId);
        assertSame(givenEntityId, actual);
    }
}
