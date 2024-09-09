package by.aurorasoft.replicator.starter;

import by.aurorasoft.replicator.factory.kafkastreams.ReplicationKafkaStreamsFactory;
import by.aurorasoft.replicator.model.setting.ReplicationConsumeSetting;
import by.aurorasoft.replicator.validator.ReplicationConsumeSettingUniqueTopicValidator;
import org.apache.kafka.streams.KafkaStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationConsumingStarterTest {

    @Mock
    private ReplicationConsumeSettingUniqueTopicValidator mockedUniqueTopicValidator;

    @Mock
    private ReplicationKafkaStreamsFactory mockedKafkaStreamsFactory;

    @Mock
    private ReplicationConsumeSetting<?, ?> firstMockedSetting;

    @Mock
    private ReplicationConsumeSetting<?, ?> secondMockedSetting;

    private ReplicationConsumingStarter starter;

    @BeforeEach
    public void initializeStarter() {
        starter = new ReplicationConsumingStarter(
                mockedUniqueTopicValidator,
                mockedKafkaStreamsFactory,
                getGivenSettings()
        );
    }

    @Test
    public void consumingShouldBeStarted() {
        KafkaStreams firstGivenKafkaStreams = mockKafkaStreamsFor(firstMockedSetting);
        KafkaStreams secondGivenKafkaStreams = mockKafkaStreamsFor(secondMockedSetting);

        starter.start();

        verify(mockedUniqueTopicValidator, times(1)).validate(eq(getGivenSettings()));
        verify(firstGivenKafkaStreams, times(1)).start();
        verify(secondGivenKafkaStreams, times(1)).start();
    }

    private List<ReplicationConsumeSetting<?, ?>> getGivenSettings() {
        return List.of(firstMockedSetting, secondMockedSetting);
    }

    private KafkaStreams mockKafkaStreamsFor(ReplicationConsumeSetting<?, ?> setting) {
        KafkaStreams kafkaStreams = mock(KafkaStreams.class);
        when(mockedKafkaStreamsFactory.create(same(setting))).thenReturn(kafkaStreams);
        return kafkaStreams;
    }
}
