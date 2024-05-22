package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
import by.aurorasoft.replicator.consuming.starter.factory.ReplicationKafkaStreamsFactory;
import org.apache.kafka.streams.KafkaStreams;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationConsumePipelineStarterTest {

    @Mock
    private ReplicationKafkaStreamsFactory mockedStreamsFactory;

    private ReplicationConsumePipelineStarter starter;

    @Before
    public void initializeStarter() {
        starter = new ReplicationConsumePipelineStarter(mockedStreamsFactory);
    }

    @Test
    public void pipelineShouldBeStarted() {
        final ReplicationConsumePipeline<?, ?> givenPipeline = mock(ReplicationConsumePipeline.class);

        final KafkaStreams givenStreams = mock(KafkaStreams.class);
        when(mockedStreamsFactory.create(same(givenPipeline))).thenReturn(givenStreams);

        starter.start(givenPipeline);

        verify(givenStreams, times(1)).start();
    }
}
