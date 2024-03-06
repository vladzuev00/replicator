package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public final class ReplicationConfigTest extends AbstractSpringBootTest {

    @Autowired
    private ReplicationProducerHolder producerHolder;

    @Test
    public void producerHolderShouldBeCreated() {
        assertNotNull(producerHolder);
    }
}
