package by.aurorasoft.replicator.factory.kafkastreams;

import by.aurorasoft.replicator.model.setting.ReplicationConsumeSetting;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ReplicationKafkaStreamsFactoryTest {

    @Mock
    private ReplicationTopologyFactory mockedTopologyFactory;

    @Mock
    private ReplicationStreamsConfigFactory mockedConfigFactory;

    @Mock
    private KafkaStreamsFactory mockedStreamsFactory;

    private ReplicationKafkaStreamsFactory replicationStreamsFactory;

    @BeforeEach
    public void initializeReplicationStreamsFactory() {
        replicationStreamsFactory = new ReplicationKafkaStreamsFactory(
                mockedTopologyFactory,
                mockedConfigFactory,
                mockedStreamsFactory
        );
    }

    @Test
    public void streamsShouldBeCreated() {
        ReplicationConsumeSetting<?, ?> givenSetting = mock(ReplicationConsumeSetting.class);

        Topology givenTopology = mock(Topology.class);
        when(mockedTopologyFactory.create(same(givenSetting))).thenReturn(givenTopology);

        StreamsConfig givenConfig = mock(StreamsConfig.class);
        when(mockedConfigFactory.create(same(givenSetting))).thenReturn(givenConfig);

        KafkaStreams givenKafkaStreams = mock(KafkaStreams.class);
        when(mockedStreamsFactory.create(same(givenTopology), same(givenConfig))).thenReturn(givenKafkaStreams);

        KafkaStreams actual = replicationStreamsFactory.create(givenSetting);
        assertSame(givenKafkaStreams, actual);
    }
}
