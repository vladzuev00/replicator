package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class TopicConfigUtil {

    public static Topic createTopicConfig(String name) {
        Topic config = mock(Topic.class);
        when(config.name()).thenReturn(name);
        return config;
    }

    public static Topic createTopicConfig(String name, int partitionCount, int replicationFactor) {
        Topic config = createTopicConfig(name);
        when(config.partitionCount()).thenReturn(partitionCount);
        when(config.replicationFactor()).thenReturn((short) replicationFactor);
        return config;
    }

    public static void checkEquals(Topic expected, Topic actual) {
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.partitionCount(), actual.partitionCount());
        assertEquals(expected.replicationFactor(), actual.replicationFactor());
    }
}
