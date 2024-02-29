package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.holder.KafkaReplicationProducerHolder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public final class ReplicationConfigTest extends AbstractSpringBootTest {

    @Autowired
    private KafkaReplicationProducerHolder producerHolder;

    @Test
    public void producerHolderShouldBeCreated() {
        assertNotNull(producerHolder);
    }
}
