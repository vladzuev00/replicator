package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationKafkaStreamsFactoryTest {

    @Mock
    private ReplicationTopologyFactory mockedTopologyFactory;

    @Mock
    private ReplicationStreamsConfigFactory mockedConfigFactory;

    @Mock
    private KafkaStreamsFactory mockedStreamsFactory;

    private ReplicationKafkaStreamsFactory replicationStreamsFactory;

    @Before
    public void initializeReplicationStreamsFactory() {
        replicationStreamsFactory = new ReplicationKafkaStreamsFactory(
                mockedTopologyFactory,
                mockedConfigFactory,
                mockedStreamsFactory
        );
    }

    @Test
    public void streamsShouldBeCreated() {
        final ReplicationConsumePipeline<?, ?> givenPipeline = mock(ReplicationConsumePipeline.class);

        final Topology givenTopology = mock(Topology.class);
        when(mockedTopologyFactory.create(same(givenPipeline))).thenReturn(givenTopology);

        final StreamsConfig givenConfig = mock(StreamsConfig.class);
        when(mockedConfigFactory.create(same(givenPipeline))).thenReturn(givenConfig);

        final KafkaStreams givenKafkaStreams = mock(KafkaStreams.class);
        when(mockedStreamsFactory.create(same(givenTopology), same(givenConfig))).thenReturn(givenKafkaStreams);

        final KafkaStreams actual = replicationStreamsFactory.create(givenPipeline);
        assertSame(givenKafkaStreams, actual);
    }
}
