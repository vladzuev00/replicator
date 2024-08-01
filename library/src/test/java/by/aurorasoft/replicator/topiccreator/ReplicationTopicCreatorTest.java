package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import by.aurorasoft.replicator.event.ReplicationTopicsCreatedEvent;
import by.aurorasoft.replicator.factory.ReplicationNewTopicFactory;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import by.aurorasoft.replicator.testrepository.FirstTestRepository;
import by.aurorasoft.replicator.testrepository.SecondTestRepository;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.LinkedHashSet;
import java.util.List;

import static by.aurorasoft.replicator.testutil.TopicConfigUtil.checkEquals;
import static by.aurorasoft.replicator.testutil.TopicConfigUtil.createTopicConfig;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationTopicCreatorTest {

    @Mock
    private ReplicatedRepositoryRegistry mockedRepositoryRegistry;

    @Mock
    private ReplicationNewTopicFactory mockedNewTopicFactory;

    @Mock
    private KafkaAdmin mockedKafkaAdmin;

    @Mock
    private ApplicationEventPublisher mockedEventPublisher;

    private ReplicationTopicCreator creator;

    @Captor
    private ArgumentCaptor<Topic> topicConfigArgumentCaptor;

    @Captor
    private ArgumentCaptor<NewTopic> newTopicArgumentCaptor;

    @Captor
    private ArgumentCaptor<ApplicationEvent> eventArgumentCaptor;

    @Before
    public void initializeCreator() {
        creator = new ReplicationTopicCreator(
                mockedRepositoryRegistry,
                mockedNewTopicFactory,
                mockedKafkaAdmin,
                mockedEventPublisher
        );
    }

    @Test
    public void topicsShouldBeCreated() {
        JpaRepository<?, ?> firstGivenRepository = new FirstTestRepository();
        JpaRepository<?, ?> secondGivenRepository = new SecondTestRepository();
        var givenRepositories = new LinkedHashSet<>(List.of(firstGivenRepository, secondGivenRepository));
        when(mockedRepositoryRegistry.getRepositories()).thenReturn(givenRepositories);

        NewTopic firstGivenNewTopic = mock(NewTopic.class);
        NewTopic secondGivenNewTopic = mock(NewTopic.class);
        when(mockedNewTopicFactory.create(any(Topic.class)))
                .thenReturn(firstGivenNewTopic)
                .thenReturn(secondGivenNewTopic);

        creator.createTopics();

        List<Topic> expectedConfigs = List.of(
                createTopicConfig("first-topic", 1, 1),
                createTopicConfig("second-topic", 2, 2)
        );
        verifyConfigs(expectedConfigs);

        List<NewTopic> expectedNewTopics = List.of(firstGivenNewTopic, secondGivenNewTopic);
        verifyCreation(expectedNewTopics);

        verifySuccessEventPublishing();
    }

    private void verifyConfigs(List<Topic> expected) {
        verify(mockedNewTopicFactory, times(expected.size())).create(topicConfigArgumentCaptor.capture());
        List<Topic> actual = topicConfigArgumentCaptor.getAllValues();
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }

    private void verifyCreation(List<NewTopic> expected) {
        verify(mockedKafkaAdmin, times(expected.size())).createOrModifyTopics(newTopicArgumentCaptor.capture());
        List<NewTopic> actual = newTopicArgumentCaptor.getAllValues();
        assertEquals(expected, actual);
    }

    private void verifySuccessEventPublishing() {
        verify(mockedEventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());
        ApplicationEvent actual = eventArgumentCaptor.getValue();
        assertTrue(actual instanceof ReplicationTopicsCreatedEvent);
        assertSame(creator, actual.getSource());
    }
}
