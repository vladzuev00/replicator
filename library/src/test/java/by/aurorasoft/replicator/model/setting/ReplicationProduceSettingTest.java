//package by.aurorasoft.replicator.model.setting;
//
//import by.aurorasoft.replicator.testentity.TestEntity;
//import by.aurorasoft.replicator.testrepository.TestRepository;
//import org.apache.kafka.common.serialization.LongSerializer;
//import org.apache.kafka.common.serialization.Serializer;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import static by.aurorasoft.replicator.model.setting.ReplicationProduceSetting.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public final class ReplicationProduceSettingTest {
//
//    @Test
//    public void settingShouldBeCreated() {
//        String givenTopic = "test-topic";
//        JpaRepository<TestEntity, Long> givenRepository = new TestRepository();
//        Serializer<Long> givenIdSerializer = new LongSerializer();
//        Integer givenBatchSize = 10;
//        Integer givenLingerMs = 11;
//        Integer givenDeliveryTimeoutMs = 12;
//        EntityViewSetting[] givenEntityViewSettings = new EntityViewSetting[]{
//                new EntityViewSetting(TestEntity.class, new String[]{"firstProperty"}),
//                new EntityViewSetting(TestEntity.class, new String[]{"secondProperty", "thirdProperty"})
//        };
//
//        ReplicationProduceSetting<TestEntity, Long> actual = new ReplicationProduceSetting<>(
//                givenTopic,
//                givenRepository,
//                givenIdSerializer,
//                givenBatchSize,
//                givenLingerMs,
//                givenDeliveryTimeoutMs,
//                givenEntityViewSettings
//        );
//
//        String actualTopic = actual.getTopic();
//        assertSame(givenTopic, actualTopic);
//
//        JpaRepository<TestEntity, Long> actualRepository = actual.getRepository();
//        assertSame(givenRepository, actualRepository);
//
//        Serializer<Long> actualIdSerializer = actual.getIdSerializer();
//        assertSame(givenIdSerializer, actualIdSerializer);
//
//        int actualBatchSize = actual.getBatchSize();
//        assertEquals(givenBatchSize, actualBatchSize);
//
//        int actualLingerMs = actual.getLingerMs();
//        assertEquals(givenLingerMs, actualLingerMs);
//
//        int actualDeliveryTimeoutMs = actual.getDeliveryTimeoutMs();
//        assertEquals(givenDeliveryTimeoutMs, actualDeliveryTimeoutMs);
//
//        EntityViewSetting[] actualEntityViewSettings = actual.getEntityViewSettings();
//        assertSame(givenEntityViewSettings, actualEntityViewSettings);
//    }
//
//    @Test
//    public void settingShouldBeCreatedWithDefaultProperties() {
//        String givenTopic = "test-topic";
//        JpaRepository<TestEntity, Long> givenRepository = new TestRepository();
//        Serializer<Long> givenIdSerializer = new LongSerializer();
//
//        ReplicationProduceSetting<TestEntity, Long> actual = ReplicationProduceSetting.<TestEntity, Long>builder()
//                .topic(givenTopic)
//                .repository(givenRepository)
//                .idSerializer(givenIdSerializer)
//                .build();
//
//        String actualTopic = actual.getTopic();
//        assertSame(givenTopic, actualTopic);
//
//        JpaRepository<TestEntity, Long> actualRepository = actual.getRepository();
//        assertSame(givenRepository, actualRepository);
//
//        Serializer<Long> actualIdSerializer = actual.getIdSerializer();
//        assertSame(givenIdSerializer, actualIdSerializer);
//
//        int actualBatchSize = actual.getBatchSize();
//        assertEquals(DEFAULT_BATCH_SIZE, actualBatchSize);
//
//        int actualLingerMs = actual.getLingerMs();
//        assertEquals(DEFAULT_LINGER_MS, actualLingerMs);
//
//        int actualDeliveryTimeoutMs = actual.getDeliveryTimeoutMs();
//        assertEquals(DEFAULT_DELIVERY_TIMEOUT_MS, actualDeliveryTimeoutMs);
//
//        EntityViewSetting[] actualEntityViewSettings = actual.getEntityViewSettings();
//        assertSame(DEFAULT_ENTITY_VIEW_SETTINGS, actualEntityViewSettings);
//    }
//
//    @Test
//    public void settingShouldNotBeCreatedBecauseOfIdSerializerIsNull() {
//        String givenTopic = "test-topic";
//       JpaRepository<TestEntity, Long> givenRepository = new TestRepository();
//
//        assertThrows(
//                NullPointerException.class,
//                () -> ReplicationProduceSetting.<TestEntity, Long>builder()
//                        .topic(givenTopic)
//                        .repository(givenRepository)
//                        .build()
//        );
//    }
//}
