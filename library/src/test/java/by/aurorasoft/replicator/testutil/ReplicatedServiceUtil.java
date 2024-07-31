package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.EntityView;
import lombok.experimental.UtilityClass;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class ReplicatedServiceUtil {

    public static ReplicatedRepository createReplicatedService(Producer producerConfig,
                                                               Topic topicConfig,
                                                               EntityView[] viewConfigs) {
        ReplicatedRepository service = mock(ReplicatedRepository.class);
        when(service.producer()).thenReturn(producerConfig);
        when(service.topicConfig()).thenReturn(topicConfig);
        when(service.entityViews()).thenReturn(viewConfigs);
        return service;
    }

    public static void checkEquals(ReplicatedRepository expected, ReplicatedRepository actual) {
        ProducerConfigUtil.checkEquals(expected.producer(), actual.producer());
        TopicConfigUtil.checkEquals(expected.topicConfig(), actual.topicConfig());
        checkEquals(expected.entityViews(), actual.entityViews());
    }

    private void checkEquals(EntityView[] expected, EntityView[] actual) {
        assertEquals(expected.length, actual.length);
        range(0, expected.length).forEach(i -> ViewConfigUtil.checkEquals(expected[i], actual[i]));
    }
}
