package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.ViewConfig;
import lombok.experimental.UtilityClass;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class ReplicatedServiceUtil {

    public static ReplicatedService createReplicatedService(ProducerConfig producerConfig,
                                                            TopicConfig topicConfig,
                                                            ViewConfig[] viewConfigs) {
        ReplicatedService service = mock(ReplicatedService.class);
        when(service.producerConfig()).thenReturn(producerConfig);
        when(service.topicConfig()).thenReturn(topicConfig);
        when(service.viewConfigs()).thenReturn(viewConfigs);
        return service;
    }

    public static void checkEquals(ReplicatedService expected, ReplicatedService actual) {
        ProducerConfigUtil.checkEquals(expected.producerConfig(), actual.producerConfig());
        TopicConfigUtil.checkEquals(expected.topicConfig(), actual.topicConfig());
        checkEquals(expected.viewConfigs(), actual.viewConfigs());
    }

    private void checkEquals(ViewConfig[] expected, ViewConfig[] actual) {
        assertEquals(expected.length, actual.length);
        range(0, expected.length).forEach(i -> ViewConfigUtil.checkEquals(expected[i], actual[i]));
    }
}
