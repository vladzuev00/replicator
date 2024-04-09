package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.base.entity.TestEntity;
import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.aurorasoft.replicator.model.consumed.SaveConsumedReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

import java.util.Properties;

import static org.apache.kafka.common.serialization.Serdes.Long;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationTopologyFactoryTest {
    private static final String GIVEN_TOPIC = "test-topic";

    private static final String DRIVER_APPLICATION_ID = "test-application";
    private static final String DRIVER_BOOTSTRAP_ADDRESS = "127.0.0.1:9092";

    @Mock
    private RetryTemplate mockedRetryTemplate;

    private ReplicationTopologyFactory topologyFactory;

    @Captor
    private ArgumentCaptor<RetryCallback<?, RuntimeException>> callbackArgumentCaptor;

    @Before
    public void initializeFactory() {
        topologyFactory = new ReplicationTopologyFactory(mockedRetryTemplate);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void topologyShouldBeCreatedAndReplicationShouldBeExecuted() {
        final JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);
        final ReplicationConsumePipeline<Long, TestEntity> givenPipeline = createPipeline(givenRepository);

        final TestEntity givenEntity = new TestEntity(255L);
        final ConsumedReplication<Long, TestEntity> givenReplication = new SaveConsumedReplication<>(givenEntity);

        try (final TopologyTestDriver driver = createDriver(givenPipeline)) {
            final TestInputTopic<Long, ConsumedReplication<Long, TestEntity>> topic = createTopic(driver);
            topic.pipeInput(givenReplication);

            verifySaveEntity(givenRepository, givenEntity);
        }
    }

    private ReplicationConsumePipeline<Long, TestEntity> createPipeline(final JpaRepository<TestEntity, Long> repository) {
        return new ReplicationConsumePipeline<>(
                DRIVER_APPLICATION_ID,
                GIVEN_TOPIC,
                Long(),
                new TypeReference<>() {
                },
                repository
        );
    }

    private TopologyTestDriver createDriver(final ReplicationConsumePipeline<Long, TestEntity> pipeline) {
        final Topology topology = topologyFactory.create(pipeline);
        final Properties properties = createDriverProperties();
        return new TopologyTestDriver(topology, properties);
    }

    private static Properties createDriverProperties() {
        final Properties properties = new Properties();
        properties.put(APPLICATION_ID_CONFIG, DRIVER_APPLICATION_ID);
        properties.put(BOOTSTRAP_SERVERS_CONFIG, DRIVER_BOOTSTRAP_ADDRESS);
        return properties;
    }

    private static TestInputTopic<Long, ConsumedReplication<Long, TestEntity>> createTopic(final TopologyTestDriver driver) {
        return driver.createInputTopic(GIVEN_TOPIC, new LongSerializer(), new JsonSerializer<>());
    }

    private void verifySaveEntity(final JpaRepository<TestEntity, Long> repository, final TestEntity entity) {
        verify(mockedRetryTemplate, times(1)).execute(callbackArgumentCaptor.capture());
        final RetryCallback<?, RuntimeException> capturedCallback = callbackArgumentCaptor.getValue();
        capturedCallback.doWithRetry(null);
        verify(repository, times(1)).save(eq(entity));
    }
}
