package by.aurorasoft.replicator.starter;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.testcrud.TestDto;
import by.aurorasoft.replicator.testcrud.TestService;
import by.aurorasoft.replicator.testutil.ReplicatedServiceUtil;
import by.aurorasoft.replicator.testutil.TopicConfigUtil;
import by.aurorasoft.replicator.topicallocator.ReplicationTopicAllocator;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static by.aurorasoft.replicator.testutil.DtoViewConfigUtil.createDtoViewConfig;
import static by.aurorasoft.replicator.testutil.ProducerConfigUtil.createProducerConfig;
import static by.aurorasoft.replicator.testutil.ReplicatedServiceUtil.createReplicatedService;
import static by.aurorasoft.replicator.testutil.TopicConfigUtil.createTopicConfig;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public final class ReplicationProducingStarterTest extends AbstractSpringBootTest {

    @MockBean
    private ReplicationTopicAllocator mockedTopicAllocator;

    @MockBean
    private ReplicationProducerFactory mockedProducerFactory;

    //to cancel creating
    @MockBean
    @SuppressWarnings("unused")
    private ReplicationProducerRegistry mockedReplicationProducerRegistry;

    @Autowired
    private ReplicationProducingStarter starter;

    @Autowired
    private TestService service;

    @Captor
    private ArgumentCaptor<TopicConfig> topicConfigCaptor;

    @Captor
    private ArgumentCaptor<ReplicatedService> serviceConfigCaptor;

    @Test
    public void producingShouldBeStarted() {
        ReplicationProducer givenProducer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(any(ReplicatedService.class))).thenReturn(givenProducer);

        ReplicationProducer actual = starter.startReturningProducer(service);
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
