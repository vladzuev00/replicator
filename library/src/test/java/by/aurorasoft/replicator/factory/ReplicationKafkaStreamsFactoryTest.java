package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationKafkaStreamsFactoryTest {

    @Mock
    private ReplicationTopologyFactory mockedTopologyFactory;

    @Mock
    private ReplicationStreamsConfigFactory mockedConfigFactory;

    private ReplicationKafkaStreamsFactory streamsFactory;

    @Before
    public void initializeStreamsFactory() {
        streamsFactory = new ReplicationKafkaStreamsFactory(mockedTopologyFactory, mockedConfigFactory);
    }

    @Test
    public void kafkaStreamsShouldBeCreated() {
        final ReplicationConsumePipeline<?, ?> givenPipeline = mock(ReplicationConsumePipeline.class);


    }
}
