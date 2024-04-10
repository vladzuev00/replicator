package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.event.ReplicationTopicsCreatedEvent;
import by.aurorasoft.replicator.factory.ReplicationTopicFactory;
import by.aurorasoft.replicator.holder.ReplicatedServiceHolder;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
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

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationTopicCreatorTest {

    @Mock
    private ReplicatedServiceHolder mockedServiceHolder;

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
        final AbsServiceRUD<?, ?, ?, ?, ?> firstGivenService = mock(AbsServiceRUD.class);
        final AbsServiceRUD<?, ?, ?, ?, ?> secondGivenService = mock(AbsServiceRUD.class);

        bindServicesWithHolder(firstGivenService, secondGivenService);

        final NewTopic firstGivenTopic = createTopicBoundedToService("first-topic", 1, 1, firstGivenService);
        final NewTopic secondGivenTopic = createTopicBoundedToService("second-topic", 2, 2, secondGivenService);

        creator.createTopics();

        verifyCreation(firstGivenTopic, secondGivenTopic);
        verifyEventPublishing();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void bindServicesWithHolder(final AbsServiceRUD<?, ?, ?, ?, ?>... services) {
        final List givenServices = asList(services);
        when(mockedServiceHolder.getServices()).thenReturn(givenServices);
    }

    private NewTopic createTopicBoundedToService(final String topicName,
                                                 final int numPartitions,
                                                 final int replicationFactor,
                                                 final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        final NewTopic topic = new NewTopic(topicName, numPartitions, (short) replicationFactor);
        when(mockedTopicFactory.create(same(service))).thenReturn(topic);
        return topic;
    }

    private void verifyCreation(final NewTopic... topics) {
        final List<NewTopic> expected = asList(topics);
        verify(mockedKafkaAdmin, times(expected.size())).createOrModifyTopics(topicArgumentCaptor.capture());
        final List<NewTopic> actual = topicArgumentCaptor.getAllValues();
        assertEquals(expected, actual);
    }

    private void verifyEventPublishing() {
        verify(mockedEventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());
        final ApplicationEvent actual = eventArgumentCaptor.getValue();
        assertTrue(actual instanceof ReplicationTopicsCreatedEvent);
        final ReplicationTopicsCreatedEvent actualTopicsCreatedEvent = (ReplicationTopicsCreatedEvent) actual;
        final Object actualSource = actualTopicsCreatedEvent.getSource();
        assertSame(creator, actualSource);
    }
}
