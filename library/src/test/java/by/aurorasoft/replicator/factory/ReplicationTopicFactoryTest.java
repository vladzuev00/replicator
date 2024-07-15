package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    private TopicConfig createTopicConfig(String name, int partitionCount, short replicationFactor) {
        TopicConfig config = mock(TopicConfig.class);
        when(config.name()).thenReturn(name);
        when(config.partitionCount()).thenReturn(partitionCount);
        when(config.replicationFactor()).thenReturn(replicationFactor);
        return config;
    }
}
