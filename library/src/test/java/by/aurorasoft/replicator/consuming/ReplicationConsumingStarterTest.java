package by.aurorasoft.replicator.consuming;

import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumer;
import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumerConfig;
import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumerStarter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationConsumingStarterTest {

    @Mock
    private ReplicationConsumerStarter mockedConsumerStarter;

    @Captor
    private ArgumentCaptor<ReplicationConsumer<?, ?>> consumerArgumentCaptor;

    @Test
    public void consumersShouldBeStarted() {
        final ReplicationConsumerConfig<?, ?> firstGivenConfig = mock(ReplicationConsumerConfig.class);
        final ReplicationConsumerConfig<?, ?> secondGivenConfig = mock(ReplicationConsumerConfig.class);
        final ReplicationConsumingStarter givenStarter = createStarter(firstGivenConfig, secondGivenConfig);

        givenStarter.start();

        verify(mockedConsumerStarter, times(2)).start(consumerArgumentCaptor.capture());

        final List<ReplicationConsumer<?, ?>> actualStartedConsumers = consumerArgumentCaptor.getAllValues();
        final List<ReplicationConsumer<?, ?>> expectedStartedConsumers = List.of(
                new ReplicationConsumer<>(firstGivenConfig),
                new ReplicationConsumer<>(secondGivenConfig)
        );
        checkEquals(expectedStartedConsumers, actualStartedConsumers);
    }

    private ReplicationConsumingStarter createStarter(final ReplicationConsumerConfig<?, ?>... consumerConfigs) {
        return new ReplicationConsumingStarter(List.of(consumerConfigs), mockedConsumerStarter);
    }

    private static void checkEquals(final List<ReplicationConsumer<?, ?>> expected,
                                    final List<ReplicationConsumer<?, ?>> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }

    private static void checkEquals(final ReplicationConsumer<?, ?> expected, final ReplicationConsumer<?, ?> actual) {
        assertSame(expected.getConfig(), actual.getConfig());
    }
}

