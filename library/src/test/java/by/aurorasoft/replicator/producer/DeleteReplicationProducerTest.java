package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class DeleteReplicationProducerTest {
    private final DeleteReplicationProducer producer = new DeleteReplicationProducer(null, null);

    @Test
    public void entityIdShouldBeGot() {
        Object givenEntityId = new Object();

        Object actual = producer.getEntityId(givenEntityId);
        assertSame(givenEntityId, actual);
    }

    @Test
    public void replicationBodyShouldBeCreated() {
        Object givenEntityId = new Object();

        Object actual = producer.createReplicationBody(givenEntityId);
        assertSame(givenEntityId, actual);
    }

    @Test
    public void replicationShouldBeCreated() {
        Object givenEntityId = new Object();

        ProducedReplication<?> actual = producer.createReplication(givenEntityId);
        ProducedReplication<?> expected = new DeleteProducedReplication(givenEntityId);
        assertEquals(expected, actual);
    }
}
