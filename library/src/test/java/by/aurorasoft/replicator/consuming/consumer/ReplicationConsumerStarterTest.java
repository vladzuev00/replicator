//package by.aurorasoft.replicator.consuming.consumer;
//
//import by.aurorasoft.replicator.consuming.starter.ReplicationConsumingPipelineStarter;
//import by.aurorasoft.replicator.factory.ReplicationConsumerContainerFactory;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.kafka.listener.MessageListenerContainer;
//
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ReplicationConsumerStarterTest {
//
//    @Mock
//    private ReplicationConsumerContainerFactory mockedContainerFactory;
//
//    private ReplicationConsumerStarter starter;
//
//    @Before
//    public void initializeStarter() {
//        starter = new ReplicationConsumerStarter(mockedContainerFactory);
//    }
//
//    @Test
//    public void consumerShouldBeStarted() {
//        final ReplicationConsumingPipelineStarter<?, ?> givenConsumer = mock(ReplicationConsumingPipelineStarter.class);
//
//        final MessageListenerContainer givenContainer = mock(MessageListenerContainer.class);
//        when(mockedContainerFactory.create(same(givenConsumer))).thenReturn(givenContainer);
//
//        starter.start(givenConsumer);
//
//        verify(givenContainer, times(1)).start();
//    }
//}
