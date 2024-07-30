package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class TopicConfigUtil {

    public static TopicConfig createTopicConfig(String name) {
        TopicConfig config = mock(TopicConfig.class);
        when(config.name()).thenReturn(name);
        return config;
    }

    public static TopicConfig createTopicConfig(String name, int partitionCount, int replicationFactor) {
        TopicConfig config = createTopicConfig(name);
        when(config.partitionCount()).thenReturn(partitionCount);
        when(config.replicationFactor()).thenReturn((short) replicationFactor);
        return config;
    }

    public static void checkEquals(TopicConfig expected, TopicConfig actual) {
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.partitionCount(), actual.partitionCount());
        assertEquals(expected.replicationFactor(), actual.replicationFactor());
    }
}
