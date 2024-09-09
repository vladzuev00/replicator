package by.aurorasoft.replicator.starter;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.testcrud.TestDto;
import by.aurorasoft.replicator.testcrud.TestService;
import by.aurorasoft.replicator.testutil.ReplicatedServiceUtil;
import by.aurorasoft.replicator.testutil.TopicConfigUtil;
import by.aurorasoft.replicator.topicallocator.ReplicationTopicAllocator;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static by.aurorasoft.replicator.testutil.DtoViewConfigUtil.createDtoViewConfig;
import static by.aurorasoft.replicator.testutil.ProducerConfigUtil.createProducerConfig;
import static by.aurorasoft.replicator.testutil.ReplicatedServiceUtil.createReplicatedService;
import static by.aurorasoft.replicator.testutil.TopicConfigUtil.createTopicConfig;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducingStarterTest {

    @Mock
    private ReplicationTopicAllocator mockedTopicAllocator;

    @Mock
    private ReplicationProducerFactory mockedProducerFactory;

    private ReplicationProducingStarter starter;

    @Captor
    private ArgumentCaptor<TopicConfig> topicConfigCaptor;

    @Captor
    private ArgumentCaptor<ReplicatedService> serviceConfigCaptor;

    @BeforeEach
    public void initializeStarter() {
        starter = new ReplicationProducingStarter(mockedTopicAllocator, mockedProducerFactory);
    }

    @Test
    public void producingShouldBeStarted() {
        TestService givenService = new TestService(null);

        ReplicationProducer givenProducer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(any(ReplicatedService.class))).thenReturn(givenProducer);

        ReplicationProducer actual = starter.startReturningProducer(givenService);
        assertSame(givenProducer, actual);

        verify(mockedTopicAllocator, times(1)).allocate(topicConfigCaptor.capture());
        TopicConfig actualTopicConfig = topicConfigCaptor.getValue();
        TopicConfig expectedTopicConfig = createTopicConfig("sync-dto", 2, 3);
        TopicConfigUtil.assertEquals(expectedTopicConfig, actualTopicConfig);

        verify(mockedProducerFactory, times(1)).create(serviceConfigCaptor.capture());
        ReplicatedService actualServiceConfig = serviceConfigCaptor.getValue();
        ReplicatedService expectedServiceConfig = createReplicatedService(
                createProducerConfig(LongSerializer.class, 11, 501, 100001),
                expectedTopicConfig,
                new DtoViewConfig[]{
                        createDtoViewConfig(
                                TestDto.class,
                                new String[]{"firstProperty"},
                                new String[]{"secondProperty"}
                        )
                }
        );
        ReplicatedServiceUtil.assertEquals(expectedServiceConfig, actualServiceConfig);
    }
}
