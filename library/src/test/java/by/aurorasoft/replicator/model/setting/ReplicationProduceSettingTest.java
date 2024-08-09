package by.aurorasoft.replicator.model.setting;

import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static by.aurorasoft.replicator.model.setting.ReplicationProduceSetting.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public final class ReplicationProduceSettingTest {

    @Test
    public void settingShouldBeCreated() {
        String givenTopic = "test-topic";
        @SuppressWarnings("unchecked") JpaRepository<Object, Object> givenRepository = mock(JpaRepository.class);
        Class<? extends Serializer<?>> givenIdSerializer = LongSerializer.class;
        Integer givenBatchSize = 10;
        Integer givenLingerMs = 11;
        Integer givenDeliveryTimeoutMs = 12;
        EntityViewSetting[] givenEntityViewSettings = new EntityViewSetting[]{
                new EntityViewSetting(Object.class, new String[]{"firstProperty"}),
                new EntityViewSetting(Object.class, new String[]{"secondProperty", "thirdProperty"})
        };

        ReplicationProduceSetting<Object, Object> actual = new ReplicationProduceSetting<>(
                givenTopic,
                givenRepository,
                givenIdSerializer,
                givenBatchSize,
                givenLingerMs,
                givenDeliveryTimeoutMs,
                givenEntityViewSettings
        );

        String actualTopic = actual.getTopic();
        assertSame(givenTopic, actualTopic);

        JpaRepository<Object, Object> actualRepository = actual.getRepository();
        assertSame(givenRepository, actualRepository);

        Class<? extends Serializer<?>> actualIdSerializer = actual.getIdSerializer();
        assertSame(givenIdSerializer, actualIdSerializer);

        int actualBatchSize = actual.getBatchSize();
        assertEquals(givenBatchSize, actualBatchSize);

        int actualLingerMs = actual.getLingerMs();
        assertEquals(givenLingerMs, actualLingerMs);

        int actualDeliveryTimeoutMs = actual.getDeliveryTimeoutMs();
        assertEquals(givenDeliveryTimeoutMs, actualDeliveryTimeoutMs);

        EntityViewSetting[] actualEntityViewSettings = actual.getEntityViewSettings();
        assertSame(givenEntityViewSettings, actualEntityViewSettings);
    }

    @Test
    public void settingShouldBeCreatedWithDefaultProperties() {
        String givenTopic = "test-topic";
        @SuppressWarnings("unchecked") JpaRepository<Object, Object> givenRepository = mock(JpaRepository.class);
        Class<? extends Serializer<?>> givenIdSerializer = LongSerializer.class;

        ReplicationProduceSetting<Object, Object> actual = ReplicationProduceSetting.builder()
                .topic(givenTopic)
                .repository(givenRepository)
                .idSerializer(givenIdSerializer)
                .build();

        String actualTopic = actual.getTopic();
        assertSame(givenTopic, actualTopic);

        JpaRepository<Object, Object> actualRepository = actual.getRepository();
        assertSame(givenRepository, actualRepository);

        Class<? extends Serializer<?>> actualIdSerializer = actual.getIdSerializer();
        assertSame(givenIdSerializer, actualIdSerializer);

        int actualBatchSize = actual.getBatchSize();
        assertEquals(DEFAULT_BATCH_SIZE, actualBatchSize);

        int actualLingerMs = actual.getLingerMs();
        assertEquals(DEFAULT_LINGER_MS, actualLingerMs);

        int actualDeliveryTimeoutMs = actual.getDeliveryTimeoutMs();
        assertEquals(DEFAULT_DELIVERY_TIMEOUT_MS, actualDeliveryTimeoutMs);

        EntityViewSetting[] actualEntityViewSettings = actual.getEntityViewSettings();
        assertSame(DEFAULT_ENTITY_VIEW_SETTINGS, actualEntityViewSettings);
    }

    @Test
    public void settingShouldNotBeCreatedBecauseOfIdSerializerIsNull() {
        String givenTopic = "test-topic";
        @SuppressWarnings("unchecked") JpaRepository<Object, Object> givenRepository = mock(JpaRepository.class);

        assertThrows(
                NullPointerException.class,
                () -> ReplicationProduceSetting.builder()
                        .topic(givenTopic)
                        .repository(givenRepository)
                        .build()
        );
    }
}
