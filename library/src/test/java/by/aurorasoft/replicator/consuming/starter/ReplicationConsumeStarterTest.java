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
    private ReplicationConsumerSetting<?, ?> firstMockedPipeline;

    @Mock
    private ReplicationConsumerSetting<?, ?> secondMockedPipeline;

    @Mock
    private ReplicationConsumePipelineStarter mockedPipelineStarter;

    private ReplicationConsumeStarter starter;

    @Captor
    private ArgumentCaptor<ReplicationConsumerSetting<?, ?>> pipelineArgumentCaptor;

    @BeforeEach
    public void initializeStarter() {
        starter = new ReplicationConsumeStarter(
                List.of(firstMockedPipeline, secondMockedPipeline),
                mockedPipelineStarter
        );
    }

    @Test
    public void consumingShouldBeStarted() {
        starter.start();

        var expectedStartedPipelines = List.of(firstMockedPipeline, secondMockedPipeline);
        verify(mockedPipelineStarter, times(expectedStartedPipelines.size())).start(pipelineArgumentCaptor.capture());
        var actualStartedPipelines = pipelineArgumentCaptor.getAllValues();
        assertEquals(expectedStartedPipelines, actualStartedPipelines);
    }
}
