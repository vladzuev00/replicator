package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.base.v2.entity.TestV2Entity;
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
    private ReplicationConsumePipeline<Long, TestV2Entity> firstMockedPipeline;

    @Mock
    private ReplicationConsumePipeline<Long, TestV2Entity> secondMockedPipeline;

    @Mock
    private ReplicationConsumePipelineStarter mockedPipelineStarter;

    private ReplicationConsumeStarter starter;

    @Captor
    private ArgumentCaptor<ReplicationConsumePipeline<Long, TestV2Entity>> pipelineArgumentCaptor;

    @Before
    public void initializeStarter() {
        starter = new ReplicationConsumeStarter(getGivenPipelines(), mockedPipelineStarter);
    }

    @Test
    public void consumingShouldBeStarted() {
        starter.start();

        final List<ReplicationConsumePipeline<?, ?>> expected = getGivenPipelines();
        verify(mockedPipelineStarter, times(expected.size())).start(pipelineArgumentCaptor.capture());
        final List<ReplicationConsumePipeline<Long, TestV2Entity>> actual = pipelineArgumentCaptor.getAllValues();
        assertEquals(expected, actual);
    }

    private List<ReplicationConsumePipeline<?, ?>> getGivenPipelines() {
        return List.of(firstMockedPipeline, secondMockedPipeline);
    }
}
