package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.base.service.SecondTestCRUDService;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class ReplicationTopicFactoryTest {
    private final ReplicationTopicFactory factory = new ReplicationTopicFactory();

    @Test
    public void topicShouldBeCreated() {
        final SecondTestCRUDService givenService = new SecondTestCRUDService();

        final NewTopic actual = factory.create(givenService);
        final NewTopic expected = new NewTopic("second-topic", 2, (short) 2);
        assertEquals(expected, actual);
    }
}
