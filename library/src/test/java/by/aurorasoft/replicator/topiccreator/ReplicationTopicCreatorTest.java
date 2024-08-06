//package by.aurorasoft.replicator.topiccreator;
//
//import by.aurorasoft.replicator.event.ReplicationTopicsCreatedEvent;
//import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.ApplicationEvent;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.kafka.core.KafkaAdmin;
//
//import java.util.LinkedHashSet;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public final class ReplicationTopicCreatorTest {
//
//    @Mock
//    private ReplicatedRepositoryRegistry mockedRepositoryRegistry;
//
//    @Mock
//    private KafkaAdmin mockedKafkaAdmin;
//
//    @Mock
//    private ApplicationEventPublisher mockedEventPublisher;
//
//    private ReplicationTopicCreator creator;
//
//    @Captor
//    private ArgumentCaptor<NewTopic> newTopicArgumentCaptor;
//
//    @Captor
//    private ArgumentCaptor<ApplicationEvent> eventArgumentCaptor;
//
//    @BeforeEach
//    public void initializeCreator() {
//        creator = new ReplicationTopicCreator(mockedRepositoryRegistry, mockedKafkaAdmin, mockedEventPublisher);
//    }
//
//    @Test
//    public void topicsShouldBeCreated() {
//        JpaRepository<?, ?> firstGivenRepository = new FirstTestRepository();
//        JpaRepository<?, ?> secondGivenRepository = new SecondTestRepository();
//        var givenRepositories = new LinkedHashSet<>(List.of(firstGivenRepository, secondGivenRepository));
//        when(mockedRepositoryRegistry.getRepositories()).thenReturn(givenRepositories);
//
//        creator.createTopics();
//
//        List<NewTopic> expectedNewTopics = List.of(
//                new NewTopic("first-topic", 1, (short) 1),
//                new NewTopic("second-topic", 2, (short) 2)
//        );
//        verifyTopicsCreation(expectedNewTopics);
//
//        verifySuccessEventPublishing();
//    }
//
//    private void verifyTopicsCreation(List<NewTopic> expected) {
//        verify(mockedKafkaAdmin, times(expected.size())).createOrModifyTopics(newTopicArgumentCaptor.capture());
//        List<NewTopic> actual = newTopicArgumentCaptor.getAllValues();
//        assertEquals(expected, actual);
//    }
//
//    private void verifySuccessEventPublishing() {
//        verify(mockedEventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());
//        ApplicationEvent actual = eventArgumentCaptor.getValue();
//        assertTrue(actual instanceof ReplicationTopicsCreatedEvent);
//        assertSame(creator, actual.getSource());
//    }
//}
