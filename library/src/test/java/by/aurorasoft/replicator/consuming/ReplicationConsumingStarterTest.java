package by.aurorasoft.replicator.consuming;

import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumer;
import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumerStarter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationConsumingStarterTest {

    @Mock
    private ReplicationConsumer<?, ?> firstMockedConsumer;

    @Mock
    private ReplicationConsumer<?, ?> secondMockedConsumer;

    @Mock
    private ReplicationConsumerStarter mockedConsumerStarter;

    private ReplicationConsumingStarter starter;

    @Captor
    private ArgumentCaptor<ReplicationConsumer<?, ?>> consumerArgumentCaptor;

    @Before
    public void initializeStarter() {
        starter = new ReplicationConsumingStarter(
                List.of(firstMockedConsumer, secondMockedConsumer),
                mockedConsumerStarter
        );
    }

    @Test
    public void consumingShouldBeStarted() {
        starter.start();

        verify(mockedConsumerStarter, times(2)).start(consumerArgumentCaptor.capture());

        final List<ReplicationConsumer<?, ?>> actualStartedConsumers = consumerArgumentCaptor.getAllValues();
        final List<ReplicationConsumer<?, ?>> expectedStartedConsumers = List.of(
                firstMockedConsumer,
                secondMockedConsumer
        );
        assertEquals(expectedStartedConsumers, actualStartedConsumers);
    }
}

