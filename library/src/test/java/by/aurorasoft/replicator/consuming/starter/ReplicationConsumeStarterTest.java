package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.factory.ReplicationKafkaStreamsFactory;
import by.aurorasoft.replicator.model.setting.ReplicationConsumerSetting;
import org.apache.kafka.streams.KafkaStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationConsumeStarterTest {

    @Mock
    private ReplicationConsumerSetting<?, ?> firstMockedConsumerSetting;

    @Mock
    private ReplicationConsumerSetting<?, ?> secondMockedConsumerSetting;

    @Mock
    private ReplicationKafkaStreamsFactory mockedKafkaStreamsFactory;

    private ReplicationConsumeStarter starter;

    @BeforeEach
    public void initializeStarter() {
        starter = new ReplicationConsumeStarter(
                List.of(firstMockedConsumerSetting, secondMockedConsumerSetting),
                mockedKafkaStreamsFactory
        );
    }

    @Test
    public void consumingShouldBeStarted() {
        KafkaStreams firstGivenKafkaStreams = mockKafkaStreamsFor(firstMockedConsumerSetting);
        KafkaStreams secondGivenKafkaStreams = mockKafkaStreamsFor(secondMockedConsumerSetting);

        starter.start();

        verify(firstGivenKafkaStreams, times(1)).start();
        verify(secondGivenKafkaStreams, times(1)).start();
    }

    private KafkaStreams mockKafkaStreamsFor(ReplicationConsumerSetting<?, ?> setting) {
        KafkaStreams kafkaStreams = mock(KafkaStreams.class);
        when(mockedKafkaStreamsFactory.create(same(setting))).thenReturn(kafkaStreams);
        return kafkaStreams;
    }
}
