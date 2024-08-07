package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.factory.kafkastreams.ReplicationStreamsConfigFactory;
import by.aurorasoft.replicator.model.setting.ReplicationConsumerSetting;
import org.apache.kafka.streams.StreamsConfig;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReplicationStreamsConfigFactoryTest {
    private static final String GIVEN_BOOTSTRAP_ADDRESS = "127.0.0.1:9092";

    private final ReplicationStreamsConfigFactory factory = new ReplicationStreamsConfigFactory(GIVEN_BOOTSTRAP_ADDRESS);

    @Test
    public void configShouldBeCreated() {
        String givenTopic = "test-topic";
        ReplicationConsumerSetting<?, ?> givenPipeline = createPipeline(givenTopic);

        StreamsConfig actual = factory.create(givenPipeline);
        StreamsConfig expected = new StreamsConfig(
                Map.of(
                        APPLICATION_ID_CONFIG, givenTopic,
                        BOOTSTRAP_SERVERS_CONFIG, GIVEN_BOOTSTRAP_ADDRESS,
                        PROCESSING_GUARANTEE_CONFIG, EXACTLY_ONCE_V2
                )
        );
        assertEquals(expected, actual);
    }

    @SuppressWarnings({"SameParameterValue"})
    private ReplicationConsumerSetting<?, ?> createPipeline(String topic) {
        return new ReplicationConsumerSetting<>(topic, null, null, null);
    }
}
