package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.factory.ReplicationTopologyFactory;
import by.aurorasoft.replicator.v2.entity.TestV2Entity;
import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import by.aurorasoft.replicator.model.replication.consumed.SaveConsumedReplication;
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
    public void topologyShouldBeCreatedAndReplicationShouldBeProducedAndConsumed() {
        final JpaRepository<TestV2Entity, Long> givenRepository = mock(JpaRepository.class);
        final ReplicationConsumePipeline<TestV2Entity, Long> givenPipeline = createPipeline(givenRepository);

        final TestV2Entity givenEntity = new TestV2Entity(255L);
        final ConsumedReplication<TestV2Entity, Long> givenReplication = new SaveConsumedReplication<>(givenEntity);

        try (final TopologyTestDriver driver = createDriver(givenPipeline)) {
            createTopic(driver).pipeInput(givenReplication);

            verifySave(givenRepository, givenEntity);
        }
    }

    private ReplicationConsumePipeline<TestV2Entity, Long> createPipeline(final JpaRepository<TestV2Entity, Long> repository) {
        return new ReplicationConsumePipeline<>(
                DRIVER_APPLICATION_ID,
                GIVEN_TOPIC,
                Long(),
                new TypeReference<>() {
                },
                repository
        );
    }

    private TopologyTestDriver createDriver(final ReplicationConsumePipeline<TestV2Entity, Long> pipeline) {
        final Topology topology = topologyFactory.create(pipeline);
        final Properties properties = createDriverProperties();
        return new TopologyTestDriver(topology, properties);
    }

    private Properties createDriverProperties() {
        final Properties properties = new Properties();
        properties.put(APPLICATION_ID_CONFIG, DRIVER_APPLICATION_ID);
        properties.put(BOOTSTRAP_SERVERS_CONFIG, DRIVER_BOOTSTRAP_ADDRESS);
        return properties;
    }

    private TestInputTopic<Long, ConsumedReplication<TestV2Entity, Long>> createTopic(final TopologyTestDriver driver) {
        return driver.createInputTopic(GIVEN_TOPIC, new LongSerializer(), new JsonSerializer<>());
    }

    private void verifySave(final JpaRepository<TestV2Entity, Long> repository, final TestV2Entity entity) {
        verify(mockedRetryTemplate, times(1)).execute(callbackArgumentCaptor.capture());
        final RetryCallback<?, RuntimeException> capturedCallback = callbackArgumentCaptor.getValue();
        capturedCallback.doWithRetry(null);
        verify(repository, times(1)).save(eq(entity));
    }
}
