package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.factory.ReplicationKafkaStreamsFactory;
import by.aurorasoft.replicator.model.provider.ReplicationConsumePipeline;
import org.apache.kafka.streams.KafkaStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationConsumePipelineStarterTest {

    @Mock
    private ReplicationKafkaStreamsFactory mockedStreamsFactory;

    private ReplicationConsumePipelineStarter starter;

    @BeforeEach
    public void initializeStarter() {
        starter = new ReplicationConsumePipelineStarter(mockedStreamsFactory);
    }

    @Test
    public void pipelineShouldBeStarted() {
        ReplicationConsumePipeline<?, ?> givenPipeline = mock(ReplicationConsumePipeline.class);

        KafkaStreams givenStreams = mock(KafkaStreams.class);
        when(mockedStreamsFactory.create(same(givenPipeline))).thenReturn(givenStreams);

        starter.start(givenPipeline);

        verify(givenStreams, times(1)).start();
    }
}
