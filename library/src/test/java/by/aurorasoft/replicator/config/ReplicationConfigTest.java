package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.support.RetryTemplate;

import static org.junit.Assert.assertNotNull;

public final class ReplicationConfigTest extends AbstractSpringBootTest {

    @Autowired
    private ReplicationProducerHolder producerHolder;

    @Autowired
    @Qualifier("replicationRetryTemplate")
    private RetryTemplate retryTemplate;

    @Test
    public void producerHolderShouldBeCreated() {
        assertNotNull(producerHolder);
    }

    @Test
    public void retryTemplateShouldBeCreated() {
        assertNotNull(retryTemplate);
    }
}
