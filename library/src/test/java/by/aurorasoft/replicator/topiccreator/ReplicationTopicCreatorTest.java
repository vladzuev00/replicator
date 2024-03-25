package by.aurorasoft.replicator.topiccreator;

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
import org.springframework.kafka.core.KafkaAdmin;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationTopicCreatorTest {

    @Mock
    private ReplicatedServiceHolder mockedServiceHolder;

    @Mock
    private ReplicationTopicFactory mockedTopicFactory;

    @Mock
    private KafkaAdmin mockedKafkaAdmin;

    private ReplicationTopicCreator creator;

    @Captor
    private ArgumentCaptor<NewTopic> topicArgumentCaptor;

    @Before
    public void initializeCreator() {
        creator = new ReplicationTopicCreator(mockedServiceHolder, mockedTopicFactory, mockedKafkaAdmin);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void topicsShouldBeCreated() {
        final AbsServiceRUD<?, ?, ?, ?, ?> firstGivenService = mock(AbsServiceRUD.class);
        final AbsServiceRUD<?, ?, ?, ?, ?> secondGivenService = mock(AbsServiceRUD.class);

        final List givenServices = List.of(firstGivenService, secondGivenService);
        when(mockedServiceHolder.getServices()).thenReturn(givenServices);

        final NewTopic firstGivenTopic = new NewTopic("first-topic", 1, (short) 1);
        when(mockedTopicFactory.create(same(firstGivenService))).thenReturn(firstGivenTopic);

        final NewTopic secondGivenTopic = new NewTopic("second-topic", 2, (short) 2);
        when(mockedTopicFactory.create(same(secondGivenService))).thenReturn(secondGivenTopic);

        creator.createTopics();

        verify(mockedKafkaAdmin, times(2)).createOrModifyTopics(topicArgumentCaptor.capture());

        final List<NewTopic> actualCreatedTopics = topicArgumentCaptor.getAllValues();
        final List<NewTopic> expectedCreatedTopics = List.of(firstGivenTopic, secondGivenTopic);
        assertEquals(expectedCreatedTopics, actualCreatedTopics);
    }
}
