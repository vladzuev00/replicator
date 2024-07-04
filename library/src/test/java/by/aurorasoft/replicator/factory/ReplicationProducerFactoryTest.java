package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.v2.service.SecondTestV2CRUDService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.junit.Assert.assertEquals;

public final class ReplicationProducerFactoryTest {
    private static final String GIVEN_BOOTSTRAP_ADDRESS = "127.0.0.1:9092";

    private static final String FIELD_NAME_PRODUCER_TOPIC_NAME = "topicName";
    private static final String FIELD_NAME_PRODUCER_KAFKA_TEMPLATE = "kafkaTemplate";

    private final ReplicationProducerFactory factory = new ReplicationProducerFactory(GIVEN_BOOTSTRAP_ADDRESS);

    @Test
    public void producerShouldBeCreated() {
        SecondTestV2CRUDService givenService = new SecondTestV2CRUDService();

        ReplicationProducer actual = factory.create(givenService);
        TestProducerMetadata actualMetadata = createMetadata(actual);
        TestProducerMetadata expectedMetadata = TestProducerMetadata.builder()
                .topicName("second-topic")
                .keySerializerType(LongSerializer.class)
                .batchSize(15)
                .lingerMs(515)
                .deliveryTimeoutMs(110000)
                .idempotenceEnable(true)
                .build();
        assertEquals(expectedMetadata, actualMetadata);
    }

    private TestProducerMetadata createMetadata(ReplicationProducer producer) {
        return TestProducerMetadata.builder()
                .topicName(getTopicName(producer))
                .keySerializerType(getKeySerializerType(producer))
                .batchSize(getBatchSize(producer))
                .lingerMs(getLingerMs(producer))
                .deliveryTimeoutMs(getDeliveryTimeout(producer))
                .idempotenceEnable(getIdempotenceEnable(producer))
                .build();
    }

    private String getTopicName(ReplicationProducer producer) {
        return getFieldValue(producer, FIELD_NAME_PRODUCER_TOPIC_NAME, String.class);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Serializer<?>> getKeySerializerType(ReplicationProducer producer) {
        return getKafkaTemplateProperty(producer, KEY_SERIALIZER_CLASS_CONFIG, Class.class);
    }

    private int getBatchSize(ReplicationProducer producer) {
        return getKafkaTemplateProperty(producer, BATCH_SIZE_CONFIG, Integer.class);
    }

    private int getLingerMs(ReplicationProducer producer) {
        return getKafkaTemplateProperty(producer, LINGER_MS_CONFIG, Integer.class);
    }

    private int getDeliveryTimeout(ReplicationProducer producer) {
        return getKafkaTemplateProperty(producer, DELIVERY_TIMEOUT_MS_CONFIG, Integer.class);
    }

    private boolean getIdempotenceEnable(ReplicationProducer producer) {
        return getKafkaTemplateProperty(producer, ENABLE_IDEMPOTENCE_CONFIG, Boolean.class);
    }

    private <P> P getKafkaTemplateProperty(ReplicationProducer producer, String propertyKey, Class<P> propertyType) {
        Object value = getKafkaTemplateProperty(producer, propertyKey);
        return propertyType.cast(value);
    }

    private Object getKafkaTemplateProperty(ReplicationProducer producer, String propertyKey) {
        return getKafkaTemplate(producer)
                .getProducerFactory()
                .getConfigurationProperties()
                .get(propertyKey);
    }

    private KafkaTemplate<?, ?> getKafkaTemplate(ReplicationProducer producer) {
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
