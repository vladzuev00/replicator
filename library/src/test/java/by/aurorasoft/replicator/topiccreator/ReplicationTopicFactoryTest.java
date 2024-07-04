package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.factory.ReplicationTopicFactory;
import by.aurorasoft.replicator.v2.service.SecondTestV2CRUDService;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class ReplicationTopicFactoryTest {
    private final ReplicationTopicFactory factory = new ReplicationTopicFactory();

    @Test
    public void topicShouldBeCreated() {
        final SecondTestV2CRUDService givenService = new SecondTestV2CRUDService();

        final NewTopic actual = factory.create(givenService);
        final NewTopic expected = new NewTopic("second-topic", 2, (short) 2);
        assertEquals(expected, actual);
    }
}
