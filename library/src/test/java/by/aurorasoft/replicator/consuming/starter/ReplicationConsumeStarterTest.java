package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
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
public final class ReplicationConsumeStarterTest {

    @Mock
    private ReplicationConsumePipeline<?, ?> firstMockedPipeline;

    @Mock
    private ReplicationConsumePipeline<?, ?> secondMockedPipeline;

    @Mock
    private ReplicationConsumePipelineStarter mockedPipelineStarter;

    private ReplicationConsumeStarter starter;

    @Captor
    private ArgumentCaptor<ReplicationConsumePipeline<?, ?>> pipelineArgumentCaptor;

    @Before
    public void initializeStarter() {
        starter = new ReplicationConsumeStarter(
                List.of(firstMockedPipeline, secondMockedPipeline),
                mockedPipelineStarter
        );
    }

    @Test
    public void consumingShouldBeStarted() {
        starter.start();

        final var expectedStartedPipelines = List.of(firstMockedPipeline, secondMockedPipeline);
        verify(mockedPipelineStarter, times(expectedStartedPipelines.size())).start(pipelineArgumentCaptor.capture());
        final var actualStartedPipelines = pipelineArgumentCaptor.getAllValues();
        assertEquals(expectedStartedPipelines, actualStartedPipelines);
    }
}
