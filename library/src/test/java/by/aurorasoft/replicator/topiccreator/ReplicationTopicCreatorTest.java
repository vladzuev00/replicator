package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.base.service.FirstTestCRUDService;
import by.aurorasoft.replicator.base.service.SecondTestCRUDService;
import by.aurorasoft.replicator.holder.ReplicatedServiceHolder;
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
    private KafkaAdmin mockedKafkaAdmin;

    private ReplicationTopicCreator creator;

    @Captor
    private ArgumentCaptor<NewTopic> topicArgumentCaptor;

    @Before
    public void initializeCreator() {
        creator = new ReplicationTopicCreator(mockedServiceHolder, mockedKafkaAdmin);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void topicsShouldBeCreated() {
        final List givenServices = List.of(new FirstTestCRUDService(), new SecondTestCRUDService());
        when(mockedServiceHolder.getServices()).thenReturn(givenServices);

        creator.createTopics();

        verify(mockedKafkaAdmin, times(2)).createOrModifyTopics(topicArgumentCaptor.capture());

        final List<NewTopic> actualCreatedTopics = topicArgumentCaptor.getAllValues();
        final List<NewTopic> expectedCreatedTopics = List.of(
                new NewTopic("first-topic", 1, (short) 1),
                new NewTopic("second-topic", 2, (short) 2)
        );
        assertEquals(expectedCreatedTopics, actualCreatedTopics);
    }
}
