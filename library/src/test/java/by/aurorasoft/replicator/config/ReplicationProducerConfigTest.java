//package by.aurorasoft.replicator.config;
//
//import by.aurorasoft.replicator.base.AbstractSpringBootTest;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static org.junit.Assert.assertEquals;
//
//public final class ReplicationProducerConfigTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private ReplicationProducerConfig config;
//
//    @Test
//    public void configShouldBeCreated() {
//        final ReplicationProducerConfig expected = ReplicationProducerConfig.builder()
//                .batchSize(10)
//                .lingerMs(500)
//                .deliveryTimeoutMs(100000)
//                .build();
//        assertEquals(expected, config);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void configShouldNotBeCreatedBecauseOfBatchSizeLessThanMinValid() {
//        ReplicationProducerConfig.builder()
//                .batchSize(0)
//                .lingerMs(500)
//                .deliveryTimeoutMs(100000)
//                .build();
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void configShouldNotBeCreatedBecauseOfBatchSizeBiggerThanMaxValid() {
//        ReplicationProducerConfig.builder()
//                .batchSize(10001)
//                .lingerMs(500)
//                .deliveryTimeoutMs(100000)
//                .build();
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void configShouldNotBeCreatedBecauseOfLingerMsLessThanMinValid() {
//        ReplicationProducerConfig.builder()
//                .batchSize(10)
//                .lingerMs(0)
//                .deliveryTimeoutMs(100000)
//                .build();
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void configShouldNotBeCreatedBecauseOfLingerMsBiggerThanMaxValid() {
//        ReplicationProducerConfig.builder()
//                .batchSize(10)
//                .lingerMs(10001)
//                .deliveryTimeoutMs(100000)
//                .build();
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void configShouldNotBeCreatedBecauseOfDeliveryTimeoutMsLessThanMinValid() {
//        ReplicationProducerConfig.builder()
//                .batchSize(10)
//                .lingerMs(500)
//                .deliveryTimeoutMs(0)
//                .build();
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void configShouldNotBeCreatedBecauseOfDeliveryTimeoutMsBiggerThanMaxValid() {
//        ReplicationProducerConfig.builder()
//                .batchSize(10)
//                .lingerMs(500)
//                .deliveryTimeoutMs(1000001)
//                .build();
//    }
//}
