package by.aurorasoft.replicator.factory.kafkastreams;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.model.setting.ReplicationConsumeSetting;
import by.aurorasoft.replicator.testcrud.TestEntity;
import by.aurorasoft.replicator.testcrud.TestRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.streams.StreamsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public final class ReplicationStreamsConfigFactoryTest extends AbstractSpringBootTest {

    @Autowired
    private ReplicationStreamsConfigFactory factory;

    @Test
    public void configShouldBeCreated() {
        String givenTopic = "test-topic";
        ReplicationConsumeSetting<TestEntity, Long> givenSetting = new ReplicationConsumeSetting<>(
                givenTopic,
                mock(TestRepository.class),
                new LongDeserializer(),
                new TypeReference<>() {
                }
        );

        StreamsConfig actual = factory.create(givenSetting);
        StreamsConfig expected = new StreamsConfig(
                Map.of(
                        APPLICATION_ID_CONFIG, givenTopic,
                        BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092",
                        PROCESSING_GUARANTEE_CONFIG, EXACTLY_ONCE_V2
                )
        );
        assertEquals(expected, actual);
    }
}
