package by.aurorasoft.replicator.factory.newtopic;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;

import static by.aurorasoft.replicator.testutil.TopicConfigUtil.createTopicConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NewTopicFactoryTest {
    private final NewTopicFactory factory = new NewTopicFactory();

    @Test
    public void newTopicShouldBeCreated() {
        String givenName = "test-topic";
        int givenPartitionCount = 1;
        short givenReplicationFactor = 2;
        TopicConfig givenConfig = createTopicConfig(givenName, givenPartitionCount, givenReplicationFactor);

        NewTopic actual = factory.create(givenConfig);
        NewTopic expected = new NewTopic(givenName, givenPartitionCount, givenReplicationFactor);
        assertEquals(expected, actual);
    }
}
