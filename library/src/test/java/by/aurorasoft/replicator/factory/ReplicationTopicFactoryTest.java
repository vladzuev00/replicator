package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;

import static by.aurorasoft.replicator.testutil.AnnotationUtil.createTopicConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReplicationTopicFactoryTest {
    private final ReplicationTopicFactory factory = new ReplicationTopicFactory();

    @Test
    public void topicShouldBeCreated() {
        String givenName = "second-topic";
        int givenPartitionCount = 2;
        short givenReplicationFactor = 3;
        TopicConfig givenConfig = createTopicConfig(givenName, givenPartitionCount, givenReplicationFactor);

        NewTopic actual = factory.create(givenConfig);
        NewTopic expected = new NewTopic(givenName, givenPartitionCount, givenReplicationFactor);
        assertEquals(expected, actual);
    }
}
