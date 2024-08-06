//package by.aurorasoft.replicator.factory;
//
//import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
//import org.junit.jupiter.api.Test;
//
//import java.util.Map;
//
//import static by.aurorasoft.replicator.testutil.ProducerConfigUtil.createProducerConfig;
//import static org.apache.kafka.clients.producer.ProducerConfig.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public final class ReplicationProducerConfigsFactoryTest {
//    private static final String GIVEN_BOOTSTRAP_ADDRESS = "127.0.0.1:9092";
//
//    private final ReplicationProducerConfigsFactory factory = new ReplicationProducerConfigsFactory(
//            GIVEN_BOOTSTRAP_ADDRESS
//    );
//
//    @Test
//    public void configsByKeysShouldBeCreated() {
//        int givenBatchSize = 5;
//        int givenLingerMs = 6;
//        int givenDeliveryTimeoutMs = 7;
//        Producer givenConfig = createProducerConfig(givenBatchSize, givenLingerMs, givenDeliveryTimeoutMs);
//
//        Map<String, Object> actual = factory.create(givenConfig);
//        Map<String, Object> expected = Map.of(
//                BOOTSTRAP_SERVERS_CONFIG, GIVEN_BOOTSTRAP_ADDRESS,
//                BATCH_SIZE_CONFIG, givenBatchSize,
//                LINGER_MS_CONFIG, givenLingerMs,
//                DELIVERY_TIMEOUT_MS_CONFIG, givenDeliveryTimeoutMs,
//                ENABLE_IDEMPOTENCE_CONFIG, true
//        );
//        assertEquals(expected, actual);
//    }
//}
