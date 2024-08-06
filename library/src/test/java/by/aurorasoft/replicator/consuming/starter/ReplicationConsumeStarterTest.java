package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.model.setting.ReplicationConsumerSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public final class ReplicationConsumeStarterTest {

    @Mock
    private ReplicationConsumerSetting<?, ?> firstMockedConsumerSetting;

    @Mock
    private ReplicationConsumerSetting<?, ?> secondMockedConsumerSetting;

    @Mock
    private ReplicationConsumerStarter mockedConsumerStarter;

    private ReplicationConsumeStarter starter;

    @Captor
    private ArgumentCaptor<ReplicationConsumerSetting<?, ?>> consumerSettingCaptor;

    @BeforeEach
    public void initializeStarter() {
        starter = new ReplicationConsumeStarter(
                List.of(firstMockedConsumerSetting, secondMockedConsumerSetting),
                mockedConsumerStarter
        );
    }

    @Test
    public void consumingShouldBeStarted() {
        starter.start();

        var expectedConsumerSettings = List.of(firstMockedConsumerSetting, secondMockedConsumerSetting);
        verify(mockedConsumerStarter, times(expectedConsumerSettings.size())).start(consumerSettingCaptor.capture());
        var actualConsumerSettings = consumerSettingCaptor.getAllValues();
        assertEquals(expectedConsumerSettings, actualConsumerSettings);
    }
}
