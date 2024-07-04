package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.v2.service.SecondTestV2CRUDService;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class ReplicationTopicFactoryTest {
    private final ReplicationTopicFactory factory = new ReplicationTopicFactory();

    @Test
    public void topicShouldBeCreated() {
        SecondTestV2CRUDService givenService = new SecondTestV2CRUDService();

        NewTopic actual = factory.create(givenService);
        NewTopic expected = new NewTopic("second-topic", 2, (short) 2);
        assertEquals(expected, actual);
    }
}
