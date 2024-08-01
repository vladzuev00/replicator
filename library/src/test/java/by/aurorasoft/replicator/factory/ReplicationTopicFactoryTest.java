package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;

import static by.aurorasoft.replicator.testutil.TopicConfigUtil.createTopicConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReplicationTopicFactoryTest {
    private final ReplicationNewTopicFactory factory = new ReplicationNewTopicFactory();

    @Test
    public void topicShouldBeCreated() {
        String givenName = "second-topic";
        int givenPartitionCount = 2;
        short givenReplicationFactor = 3;
        Topic givenConfig = createTopicConfig(givenName, givenPartitionCount, givenReplicationFactor);

        NewTopic actual = factory.create(givenConfig);
        NewTopic expected = new NewTopic(givenName, givenPartitionCount, givenReplicationFactor);
        assertEquals(expected, actual);
    }
}
