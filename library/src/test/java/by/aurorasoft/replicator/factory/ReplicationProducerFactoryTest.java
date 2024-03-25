package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.base.service.SecondTestCRUDService;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.junit.Assert.assertEquals;

public final class ReplicationProducerFactoryTest {
    private static final String GIVEN_BOOTSTRAP_ADDRESS = "127.0.0.1:9092";

    private static final String FIELD_NAME_PRODUCER_TOPIC_NAME = "topicName";
    private static final String FIELD_NAME_PRODUCER_KAFKA_TEMPLATE = "kafkaTemplate";

    private static final Class<? extends Serializer<?>> EXPECTED_KEY_SERIALIZER_TYPE = LongSerializer.class;
    private static final boolean EXPECTED_IDEMPOTENCE_ENABLE = true;

    private final ReplicationProducerFactory factory = new ReplicationProducerFactory(GIVEN_BOOTSTRAP_ADDRESS);

    @Test
    public void producerShouldBeCreated() {
        final SecondTestCRUDService givenService = new SecondTestCRUDService();

        final ReplicationProducer<Long> actual = factory.create(givenService);
        final TestProducerMetadata actualMetadata = createMetadata(actual);
        final TestProducerMetadata expectedMetadata = createExpectedProducerMetadata(
                "second-topic",
                15,
                515,
                110000
        );
        assertEquals(expectedMetadata, actualMetadata);
    }

    @SuppressWarnings("SameParameterValue")
    private TestProducerMetadata createExpectedProducerMetadata(final String topicName,
                                                                final int batchSize,
                                                                final int lingerMs,
                                                                final int deliveryTimeoutMs) {
        return TestProducerMetadata.builder()
                .topicName(topicName)
                .keySerializerType(EXPECTED_KEY_SERIALIZER_TYPE)
                .batchSize(batchSize)
                .lingerMs(lingerMs)
                .deliveryTimeoutMs(deliveryTimeoutMs)
                .idempotenceEnable(EXPECTED_IDEMPOTENCE_ENABLE)
                .build();
    }

    private static TestProducerMetadata createMetadata(final ReplicationProducer<?> producer) {
        return TestProducerMetadata.builder()
                .topicName(getTopicName(producer))
                .keySerializerType(getKeySerializerType(producer))
                .batchSize(getBatchSize(producer))
                .lingerMs(getLingerMs(producer))
                .deliveryTimeoutMs(getDeliveryTimeout(producer))
                .idempotenceEnable(getIdempotenceEnable(producer))
                .build();
    }

    private static String getTopicName(final ReplicationProducer<?> producer) {
        return getFieldValue(producer, FIELD_NAME_PRODUCER_TOPIC_NAME, String.class);
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Serializer<?>> getKeySerializerType(final ReplicationProducer<?> producer) {
        return getKafkaTemplateProperty(producer, KEY_SERIALIZER_CLASS_CONFIG, Class.class);
    }

    private static int getBatchSize(final ReplicationProducer<?> producer) {
        return getKafkaTemplateProperty(producer, BATCH_SIZE_CONFIG, Integer.class);
    }

    private static int getLingerMs(final ReplicationProducer<?> producer) {
        return getKafkaTemplateProperty(producer, LINGER_MS_CONFIG, Integer.class);
    }

    private static int getDeliveryTimeout(final ReplicationProducer<?> producer) {
        return getKafkaTemplateProperty(producer, DELIVERY_TIMEOUT_MS_CONFIG, Integer.class);
    }

    private static boolean getIdempotenceEnable(final ReplicationProducer<?> producer) {
        return getKafkaTemplateProperty(producer, ENABLE_IDEMPOTENCE_CONFIG, Boolean.class);
    }

    private static <P> P getKafkaTemplateProperty(final ReplicationProducer<?> producer,
                                                  final String propertyKey,
                                                  final Class<P> propertyType) {
        final Object value = getKafkaTemplateProperty(producer, propertyKey);
        return propertyType.cast(value);
    }

    private static Object getKafkaTemplateProperty(final ReplicationProducer<?> producer, final String propertyKey) {
        return getKafkaTemplate(producer)
                .getProducerFactory()
                .getConfigurationProperties()
                .get(propertyKey);
    }

    private static KafkaTemplate<?, ?> getKafkaTemplate(final ReplicationProducer<?> producer) {
        return getFieldValue(producer, FIELD_NAME_PRODUCER_KAFKA_TEMPLATE, KafkaTemplate.class);
    }

    @Value
    @AllArgsConstructor
    @Builder
    private static class TestProducerMetadata {
        Class<? extends Serializer<?>> keySerializerType;
        int batchSize;
        int lingerMs;
        int deliveryTimeoutMs;
        boolean idempotenceEnable;
        String topicName;
    }
}
