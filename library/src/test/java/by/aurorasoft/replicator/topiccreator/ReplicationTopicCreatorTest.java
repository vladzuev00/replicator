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

        final NewTopic firstGivenTopic = new NewTopic("first-topic", 1, (short) 1);
        bindServiceWithTopic(firstGivenService, firstGivenTopic);

        final NewTopic secondGivenTopic = new NewTopic("second-topic", 2, (short) 2);
        bindServiceWithTopic(secondGivenService, secondGivenTopic);

        creator.createTopics();

        verifyTopicCreating(firstGivenTopic, secondGivenTopic);
        verifyEventPublishing();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void bindServicesWithHolder(final AbsServiceRUD<?, ?, ?, ?, ?>... services) {
        final List givenServices = asList(services);
        when(mockedServiceHolder.getServices()).thenReturn(givenServices);
    }

    private void bindServiceWithTopic(final AbsServiceRUD<?, ?, ?, ?, ?> service, final NewTopic topic) {
        when(mockedTopicFactory.create(same(service))).thenReturn(topic);
    }

    private void verifyTopicCreating(final NewTopic... topics) {
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
