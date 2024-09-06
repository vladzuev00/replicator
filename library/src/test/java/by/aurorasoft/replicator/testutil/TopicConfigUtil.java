package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import lombok.experimental.UtilityClass;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class TopicConfigUtil {

    public static TopicConfig createTopicConfig(String name) {
        TopicConfig config = mock(TopicConfig.class);
        when(config.name()).thenReturn(name);
        return config;
    }
}
