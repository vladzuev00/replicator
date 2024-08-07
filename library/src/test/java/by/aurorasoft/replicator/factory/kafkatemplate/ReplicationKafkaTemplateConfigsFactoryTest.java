package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReplicationKafkaTemplateConfigsFactoryTest extends AbstractSpringBootTest {

    @Autowired
    private ReplicationKafkaTemplateConfigsFactory factory;

    @Test
    public void configsByKeysShouldBeCreated() {
        int givenBatchSize = 10;
        int givenLingerMs = 11;
        int givenDeliveryTimeoutMs = 12;
        ReplicationProducerSetting<?, ?> givenSetting = ReplicationProducerSetting.builder()
                .batchSize(givenBatchSize)
                .lingerMs(givenLingerMs)
                .deliveryTimeoutMs(givenDeliveryTimeoutMs)
                .build();

        Map<String, Object> actual = factory.create(givenSetting);
        Map<String, Object> expected = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092",
                BATCH_SIZE_CONFIG, givenBatchSize,
                LINGER_MS_CONFIG, givenLingerMs,
                DELIVERY_TIMEOUT_MS_CONFIG, givenDeliveryTimeoutMs,
                ENABLE_IDEMPOTENCE_CONFIG, true
        );
        assertEquals(expected, actual);
    }
}