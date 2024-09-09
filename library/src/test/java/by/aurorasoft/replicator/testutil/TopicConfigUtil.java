package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;

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
        TopicConfig config = mock(TopicConfig.class);
        when(config.name()).thenReturn(name);
        when(config.partitionCount()).thenReturn(partitionCount);
        when(config.replicationFactor()).thenReturn((short) replicationFactor);
        return config;
    }

    public static void assertEquals(TopicConfig expected, TopicConfig actual) {
        Assertions.assertEquals(expected.name(), actual.name());
        Assertions.assertEquals(expected.partitionCount(), actual.partitionCount());
        Assertions.assertEquals(expected.replicationFactor(), actual.replicationFactor());
    }
}
