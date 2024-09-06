package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import lombok.experimental.UtilityClass;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class ReplicatedServiceUtil {

    public static ReplicatedService createReplicatedService(ProducerConfig producerConfig,
                                                            TopicConfig topicConfig,
                                                            DtoViewConfig[] dtoViewConfigs) {
        ReplicatedService replicatedService = mock(ReplicatedService.class);
        when(replicatedService.producerConfig()).thenReturn(producerConfig);
        when(replicatedService.topicConfig()).thenReturn(topicConfig);
        when(replicatedService.dtoViewConfigs()).thenReturn(dtoViewConfigs);
        return replicatedService;
    }

    public static void assertEquals(ReplicatedService expected, ReplicatedService actual) {
        ProducerConfigUtil.assertEquals(expected.producerConfig(), actual.producerConfig());
        TopicConfigUtil.assertEquals(expected.topicConfig(), actual.topicConfig());
        DtoViewConfigUtil.assertEquals(expected.dtoViewConfigs(), actual.dtoViewConfigs());
    }
}
