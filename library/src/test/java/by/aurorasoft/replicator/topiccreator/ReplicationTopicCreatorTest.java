package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.event.ReplicationTopicsCreatedEvent;
import by.aurorasoft.replicator.factory.ReplicationTopicFactory;
import by.aurorasoft.replicator.registry.ReplicatedServiceRegistry;
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

import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationTopicCreatorTest {

    @Mock
    private ReplicatedServiceRegistry mockedServiceHolder;

    @Mock
    private ReplicationTopicFactory mockedTopicFactory;

    @Mock
    private KafkaAdmin mockedKafkaAdmin;

    @Mock
    private ApplicationEventPublisher mockedEventPublisher;

    private ReplicationTopicCreator creator;

    @Captor
    private ArgumentCaptor<NewTopic> topicArgumentCaptor;

    @Captor
    private ArgumentCaptor<ApplicationEvent> eventArgumentCaptor;

    @Before
    public void initializeCreator() {
        creator = new ReplicationTopicCreator(
                mockedServiceHolder,
                mockedTopicFactory,
                mockedKafkaAdmin,
                mockedEventPublisher
        );
    }

    @Test
    public void topicsShouldBeCreated() {
        final Object firstGivenService = new Object();
        final Object secondGivenService = new Object();
        final Set<Object> givenServices = Set.of(firstGivenService, secondGivenService);
        when(mockedServiceHolder.getServices()).thenReturn(givenServices);

        final NewTopic firstGivenTopic = mockTopicForService("first-topic", 1, 1, firstGivenService);
        final NewTopic secondGivenTopic = mockTopicForService("second-topic", 2, 2, secondGivenService);

        creator.createTopics();

        verifyCreation(firstGivenTopic, secondGivenTopic);
        verifySuccessEventPublishing();
    }

    private NewTopic mockTopicForService(final String topicName,
                                         final int numPartitions,
                                         final int replicationFactor,
                                         final Object service) {
        final NewTopic topic = new NewTopic(topicName, numPartitions, (short) replicationFactor);
        when(mockedTopicFactory.create(same(service))).thenReturn(topic);
        return topic;
    }

    private void verifyCreation(final NewTopic... topics) {
        verify(mockedKafkaAdmin, times(topics.length)).createOrModifyTopics(topicArgumentCaptor.capture());
        final List<NewTopic> actual = topicArgumentCaptor.getAllValues();
        final List<NewTopic> expected = asList(topics);
        assertEquals(expected, actual);
    }

    private void verifySuccessEventPublishing() {
        verify(mockedEventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());
        final ApplicationEvent actual = eventArgumentCaptor.getValue();
        assertTrue(actual instanceof ReplicationTopicsCreatedEvent);
        assertSame(creator, actual.getSource());
    }
}
