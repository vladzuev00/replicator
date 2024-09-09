package by.aurorasoft.replicator.starter;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.topicallocator.ReplicationTopicAllocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static by.aurorasoft.replicator.testutil.ReplicatedServiceUtil.createReplicatedService;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducingStarterTest {

    @Mock
    private ReplicationTopicAllocator mockedTopicAllocator;

    @Mock
    private ReplicationProducerFactory mockedProducerFactory;

    private ReplicationProducingStarter starter;

    @BeforeEach
    public void initializeStarter() {
        starter = new ReplicationProducingStarter(mockedTopicAllocator, mockedProducerFactory);
    }

    @Test
    public void producingShouldBeStarted() {
        TopicConfig givenTopicConfig = mock(TopicConfig.class);
        ReplicatedService givenService = createReplicatedService(givenTopicConfig);

        ReplicationProducer givenProducer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(same(givenService))).thenReturn(givenProducer);

        ReplicationProducer actual = starter.start(givenService);
        assertSame(givenProducer, actual);

        verify(mockedTopicAllocator, times(1)).allocate(same(givenTopicConfig));
    }
}
