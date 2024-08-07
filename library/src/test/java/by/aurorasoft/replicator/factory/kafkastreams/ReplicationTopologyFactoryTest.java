package by.aurorasoft.replicator.factory.kafkastreams;

import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import by.aurorasoft.replicator.model.replication.consumed.SaveConsumedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationConsumerSetting;
import by.aurorasoft.replicator.testentity.TestEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationTopologyFactoryTest {
    private static final String GIVEN_TOPIC = "test-topic";

    private static final String DRIVER_APPLICATION_ID = "test-application";
    private static final String DRIVER_BOOTSTRAP_ADDRESS = "127.0.0.1:9092";

    @Mock
    private RetryTemplate mockedRetryTemplate;

    private ReplicationTopologyFactory topologyFactory;

    @Captor
    private ArgumentCaptor<RetryCallback<?, RuntimeException>> callbackCaptor;

    @BeforeEach
    public void initializeFactory() {
        topologyFactory = new ReplicationTopologyFactory(mockedRetryTemplate);
    }

    @Test
    public void topologyShouldBeCreatedAndReplicationShouldBeProducedAndConsumed() {
        @SuppressWarnings("unchecked") JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);
        ReplicationConsumerSetting<TestEntity, Long> givenSetting = createSetting(givenRepository);

        TestEntity givenEntity = TestEntity.builder()
                .id(255L)
                .build();
        ConsumedReplication<TestEntity, Long> givenReplication = new SaveConsumedReplication<>(givenEntity);

        try (TopologyTestDriver driver = createDriver(givenSetting)) {
            createTopic(driver).pipeInput(givenReplication);

            verifySave(givenRepository, givenEntity);
        }
    }

    private ReplicationConsumerSetting<TestEntity, Long> createSetting(JpaRepository<TestEntity, Long> repository) {
        return new ReplicationConsumerSetting<>(
                GIVEN_TOPIC,
                repository,
                new LongDeserializer(),
                new TypeReference<>() {
                }
        );
    }

    private TopologyTestDriver createDriver(ReplicationConsumerSetting<TestEntity, Long> setting) {
        Topology topology = topologyFactory.create(setting);
        Properties properties = createDriverProperties();
        return new TopologyTestDriver(topology, properties);
    }

    private Properties createDriverProperties() {
        Properties properties = new Properties();
        properties.put(APPLICATION_ID_CONFIG, DRIVER_APPLICATION_ID);
        properties.put(BOOTSTRAP_SERVERS_CONFIG, DRIVER_BOOTSTRAP_ADDRESS);
        return properties;
    }

    private TestInputTopic<Long, ConsumedReplication<TestEntity, Long>> createTopic(TopologyTestDriver driver) {
        return driver.createInputTopic(GIVEN_TOPIC, new LongSerializer(), new JsonSerializer<>());
    }

    private void verifySave(JpaRepository<TestEntity, Long> repository, TestEntity entity) {
        verify(mockedRetryTemplate, times(1)).execute(callbackCaptor.capture());
        RetryCallback<?, RuntimeException> capturedCallback = callbackCaptor.getValue();
        capturedCallback.doWithRetry(null);
        verify(repository, times(1)).save(eq(entity));
    }
}
