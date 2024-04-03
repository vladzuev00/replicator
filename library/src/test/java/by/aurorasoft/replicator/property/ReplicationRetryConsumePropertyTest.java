package by.aurorasoft.replicator.property;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class ReplicationRetryConsumePropertyTest extends AbstractSpringBootTest {

    @Autowired
    private ReplicationRetryConsumeProperty actual;

    @Test
    public void propertyShouldBeCreated() {
        final ReplicationRetryConsumeProperty expected = new ReplicationRetryConsumeProperty(2000, 3);
        assertEquals(expected, actual);
    }
}
