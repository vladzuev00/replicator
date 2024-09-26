package by.aurorasoft.replicator.topicallocator;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.factory.newtopic.NewTopicFactory;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaAdmin;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationTopicAllocatorTest {

    @Mock
    private NewTopicFactory mockedTopicFactory;

    @Mock
    private KafkaAdmin mockedKafkaAdmin;

    private ReplicationTopicAllocator allocator;

    @BeforeEach
    public void initializeAllocator() {
        allocator = new ReplicationTopicAllocator(mockedTopicFactory, mockedKafkaAdmin);
    }

    @Test
    public void topicShouldBeAllocated() {
        TopicConfig givenConfig = mock(TopicConfig.class);

        NewTopic givenTopic = mock(NewTopic.class);
        when(mockedTopicFactory.create(same(givenConfig))).thenReturn(givenTopic);

        allocator.allocate(givenConfig);

        verify(mockedKafkaAdmin, times(1)).createOrModifyTopics(same(givenTopic));
    }
}
