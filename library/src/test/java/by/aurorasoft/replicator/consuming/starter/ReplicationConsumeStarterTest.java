package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.base.entity.TestEntity;
import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import org.apache.kafka.streams.StreamsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationConsumeStarterTest {

    @Mock
    private ReplicationConsumePipeline<Long, TestEntity> firstMockedPipeline;

    @Mock
    private ReplicationConsumePipeline<Long, TestEntity> secondMockedPipeline;

    @Mock
    private ReplicationConsumePipelineStarter mockedPipelineStarter;

    @Mock
    private StreamsBuilder mockedStreamsBuilder;

    private ReplicationConsumeStarter starter;

    @Captor
    private ArgumentCaptor<ReplicationConsumePipeline<Long, TestEntity>> pipelineArgumentCaptor;

    @Before
    public void initializeStarter() {
        starter = new ReplicationConsumeStarter(
                List.of(firstMockedPipeline, secondMockedPipeline),
                mockedPipelineStarter,
                mockedStreamsBuilder
        );
    }

    @Test
    public void consumingShouldBeStarted() {
        starter.start();

        verify(mockedPipelineStarter, times(2)).start(
                pipelineArgumentCaptor.capture(),
                same(mockedStreamsBuilder)
        );

        final List<ReplicationConsumePipeline<Long, TestEntity>> actual = pipelineArgumentCaptor.getAllValues();
        final List<ReplicationConsumePipeline<Long, TestEntity>> expected = List.of(
                firstMockedPipeline,
                secondMockedPipeline
        );
        assertEquals(expected, actual);
    }
}
