package by.aurorasoft.replicator.model.replication.produced;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class DeleteProducedReplicationTest {

    @Test
    public void entityIdShouldBeGot() {
        final Object givenEntityId = new Object();
        final DeleteProducedReplication givenReplication = new DeleteProducedReplication(null);

        final Object actual = givenReplication.getEntityId(givenEntityId);
        assertSame(givenEntityId, actual);
    }
}
