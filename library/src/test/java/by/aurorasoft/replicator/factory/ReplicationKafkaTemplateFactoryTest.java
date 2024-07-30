package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.objectmapper.ReplicationObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ReplicationKafkaTemplateFactoryTest {
    private static final String GIVEN_BOOTSTRAP_ADDRESS = "127.0.0.1:9092";

    private static final String FIELD_NAME_OBJECT_MAPPER = "objectMapper";

    @Mock
    private ReplicationObjectMapper mockedObjectMapper;

    private ReplicationKafkaTemplateFactory factory;

    @BeforeEach
    public void initializeFactory() {
        factory = new ReplicationKafkaTemplateFactory(mockedObjectMapper, GIVEN_BOOTSTRAP_ADDRESS);
    }

    @Test
    public void templateShouldBeCreated() {
        Class<?> givenIdSerializerType = LongSerializer.class;
        int givenBatchSize = 10;
        int givenLingerMs = 500;
        int givenDeliveryTimeoutMs = 100000;
        ProducerConfig givenConfig = createProducerConfig(
                givenIdSerializerType,
                givenBatchSize,
                givenLingerMs,
                givenDeliveryTimeoutMs
        );

        KafkaTemplate<Object, ProducedReplication<?>> actual = factory.create(givenConfig);
        assertNotNull(actual);

        ProducerFactory<Object, ProducedReplication<?>> actualProducerFactory = actual.getProducerFactory();
        assertNotNull(actualProducerFactory);

        Map<String, Object> actualConfigsByKeys = actualProducerFactory.getConfigurationProperties();
        Map<String, Object> expectedConfigsByKeys = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, GIVEN_BOOTSTRAP_ADDRESS,
                BATCH_SIZE_CONFIG, givenBatchSize,
                LINGER_MS_CONFIG, givenLingerMs,
                DELIVERY_TIMEOUT_MS_CONFIG, givenDeliveryTimeoutMs,
                ENABLE_IDEMPOTENCE_CONFIG, true
        );
        assertEquals(expectedConfigsByKeys, actualConfigsByKeys);

        Serializer<Object> actualKeySerializer = actualProducerFactory.getKeySerializer();
        assertNotNull(actualKeySerializer);
        Class<?> actualKeySerializerType = actualKeySerializer.getClass();
        assertSame(givenIdSerializerType, actualKeySerializerType);

        var actualValueSerializer = (JsonSerializer<ProducedReplication<?>>) actualProducerFactory.getValueSerializer();
        ObjectMapper actualObjectMapper = getObjectMapper(actualValueSerializer);
        assertSame(mockedObjectMapper, actualObjectMapper);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ProducerConfig createProducerConfig(Class idSerializer,
                                                int batchSize,
                                                int lingerMs,
                                                int deliveryTimeoutMs) {
        ProducerConfig config = mock(ProducerConfig.class);
        when(config.idSerializer()).thenReturn(idSerializer);
        when(config.batchSize()).thenReturn(batchSize);
        when(config.lingerMs()).thenReturn(lingerMs);
        when(config.deliveryTimeoutMs()).thenReturn(deliveryTimeoutMs);
        return config;
    }

    private ObjectMapper getObjectMapper(JsonSerializer<ProducedReplication<?>> serializer) {
        return getFieldValue(serializer, FIELD_NAME_OBJECT_MAPPER, ObjectMapper.class);
    }
}
