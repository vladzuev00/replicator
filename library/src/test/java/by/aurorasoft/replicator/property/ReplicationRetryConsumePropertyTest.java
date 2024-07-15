package by.aurorasoft.replicator.property;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        properties = {
                "replication.consume.retry.time-lapse-ms=3000",
                "replication.consume.retry.max-attempts=100"
        }
)
public final class ReplicationRetryConsumePropertyTest extends AbstractSpringBootTest {

    @Autowired
    private ReplicationRetryConsumeProperty actual;

    @Test
    public void propertyShouldBeCreated() {
        ReplicationRetryConsumeProperty expected = new ReplicationRetryConsumeProperty(3000, 100);
        assertEquals(expected, actual);
    }
}
