package by.aurorasoft.replicator.model.replication.produced;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;


public final class DeleteProducedReplicationTest {

    @Test
    public void entityIdShouldBeGot() {
        Object givenEntityId = new Object();
        DeleteProducedReplication givenReplication = new DeleteProducedReplication(null);

        Object actual = givenReplication.getEntityId(givenEntityId);
        assertSame(givenEntityId, actual);
    }
}
