package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.event.ReplicationTopicsCreatedEvent;
import by.aurorasoft.replicator.factory.ReplicationTopicFactory;
import by.aurorasoft.replicator.registry.ReplicatedServiceRegistry;
import by.aurorasoft.replicator.v1.service.FirstTestV1CRUDService;
import by.aurorasoft.replicator.v2.service.SecondTestV2CRUDService;
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
import org.springframework.kafka.core.KafkaAdmin;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationTopicCreatorTest {

    @Mock
    private ReplicatedServiceRegistry mockedServiceRegistry;

    @Mock
    private ReplicationTopicFactory mockedTopicFactory;

    @Mock
    private KafkaAdmin mockedKafkaAdmin;

    @Mock
    private ApplicationEventPublisher mockedEventPublisher;

    private ReplicationTopicCreator creator;

    @Captor
    private ArgumentCaptor<TopicConfig> topicConfigArgumentCaptor;

    @Captor
    private ArgumentCaptor<NewTopic> topicArgumentCaptor;

    @Captor
    private ArgumentCaptor<ApplicationEvent> eventArgumentCaptor;

    @Before
    public void initializeCreator() {
        creator = new ReplicationTopicCreator(
                mockedServiceRegistry,
                mockedTopicFactory,
                mockedKafkaAdmin,
                mockedEventPublisher
        );
    }

    @Test
    public void topicsShouldBeCreated() {
        Object firstGivenService = new FirstTestV1CRUDService();
        Object secondGivenService = new SecondTestV2CRUDService();
        Set<Object> givenServices = new LinkedHashSet<>(List.of(firstGivenService, secondGivenService));
        when(mockedServiceRegistry.getServices()).thenReturn(givenServices);

        NewTopic firstGivenTopic = mock(NewTopic.class);
        NewTopic secondGivenTopic = mock(NewTopic.class);
        when(mockedTopicFactory.create(any(TopicConfig.class)))
                .thenReturn(firstGivenTopic)
                .thenReturn(secondGivenTopic);

        creator.createTopics();

        List<TopicConfig> expectedConfigs = List.of(
                createTopicConfig("first-topic", 1, 1),
                createTopicConfig("second-topic", 2, 2)
        );
        verifyConfigs(expectedConfigs);

        verifyCreation(firstGivenTopic, secondGivenTopic);
        verifySuccessEventPublishing();
    }

    private void verifyConfigs(List<TopicConfig> expected) {
        verify(mockedTopicFactory, times(expected.size())).create(topicConfigArgumentCaptor.capture());
        List<TopicConfig> actual = topicConfigArgumentCaptor.getAllValues();
        checkEquals(expected, actual);
    }

    private void checkEquals(List<TopicConfig> expected, List<TopicConfig> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }

    private void checkEquals(TopicConfig expected, TopicConfig actual) {
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.partitionCount(), actual.partitionCount());
        assertEquals(expected.replicationFactor(), actual.replicationFactor());
    }

    private TopicConfig createTopicConfig(String name, int partitionCount, int replicationFactor) {
        TopicConfig config = mock(TopicConfig.class);
        when(config.name()).thenReturn(name);
        when(config.partitionCount()).thenReturn(partitionCount);
        when(config.replicationFactor()).thenReturn((short) replicationFactor);
        return config;
    }

    private void verifyCreation(NewTopic... topics) {
        verify(mockedKafkaAdmin, times(topics.length)).createOrModifyTopics(topicArgumentCaptor.capture());
        List<NewTopic> actual = topicArgumentCaptor.getAllValues();
        List<NewTopic> expected = List.of(topics);
        assertEquals(expected, actual);
    }

    private void verifySuccessEventPublishing() {
        verify(mockedEventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());
        ApplicationEvent actual = eventArgumentCaptor.getValue();
        assertTrue(actual instanceof ReplicationTopicsCreatedEvent);
        assertSame(creator, actual.getSource());
    }
}
