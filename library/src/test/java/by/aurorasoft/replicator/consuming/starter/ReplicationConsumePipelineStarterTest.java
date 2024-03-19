package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.base.entity.TestEntity;
import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.aurorasoft.replicator.model.consumed.DeleteConsumedReplication;
import by.aurorasoft.replicator.model.consumed.SaveConsumedReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.support.serializer.JsonSerializer;

import static org.apache.kafka.common.serialization.Serdes.Long;

//TODO: realise
@RunWith(MockitoJUnitRunner.class)
public final class ReplicationConsumePipelineStarterTest {
    private static final String GIVEN_TOPIC = "sync-dto";

    private final ReplicationConsumePipelineStarter starter = new ReplicationConsumePipelineStarter();

    @Mock
    private JpaRepository<TestEntity, Long> mockedRepository;

    @Test
    public void pipelineShouldBeStartedAndReceiveReplications() {
        final ReplicationConsumePipeline<Long, TestEntity> givenPipeline = createPipeline();
        final StreamsBuilder givenStreamBuilder = new StreamsBuilder();

        starter.start(givenPipeline, givenStreamBuilder);

        final Topology topology = givenStreamBuilder.build();
        final TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology);

        final TestInputTopic<Long, ConsumedReplication<Long, TestEntity>> givenTopic = topologyTestDriver
                .createInputTopic(GIVEN_TOPIC, new LongSerializer(), new JsonSerializer<>());

        givenTopic.pipeInput(new SaveConsumedReplication<>(new TestEntity(255L)));
        givenTopic.pipeInput(new DeleteConsumedReplication<>(255L));

        Mockito.verify(mockedRepository, Mockito.times(1)).save(Mockito.eq(new TestEntity(255L)));
        Mockito.verify(mockedRepository, Mockito.times(1)).deleteById(Mockito.eq(255L));
    }

    private ReplicationConsumePipeline<Long, TestEntity> createPipeline() {
        return new ReplicationConsumePipeline<>(
                GIVEN_TOPIC,
                Long(),
                new TypeReference<>() {
                },
                mockedRepository
        );
    }
}
