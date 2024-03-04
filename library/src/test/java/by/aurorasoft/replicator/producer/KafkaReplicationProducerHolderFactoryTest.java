package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.base.service.FirstTestCRUDService;
import by.aurorasoft.replicator.base.service.SecondTestCRUDService;
import by.aurorasoft.replicator.holder.KafkaReplicationProducerHolder;
import by.aurorasoft.replicator.holder.ReplicatedServiceHolder;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
import static java.util.stream.Collectors.toMap;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaReplicationProducerHolderFactoryTest {
    private static final String FIELD_NAME_HOLDER_PRODUCERS_BY_SERVICES = "producersByServices";

    private static final String FIELD_NAME_PRODUCER_TOPIC_NAME = "topicName";
    private static final String FIELD_NAME_PRODUCER_OBJECT_MAPPER = "objectMapper";
    private static final String FIELD_NAME_PRODUCER_KAFKA_TEMPLATE = "kafkaTemplate";

    private static final String GIVEN_BOOTSTRAP_ADDRESS = "127.0.0.1:9092";
    private static final Class<? extends Serializer<?>> GIVEN_KEY_SERIALIZER_TYPE = LongSerializer.class;

    @Mock
    private ReplicatedServiceHolder mockedReplicatedServiceHolder;

    @Mock
    private ObjectMapper mockedObjectMapper;

    private KafkaReplicationProducerHolderFactory factory;

    @Before
    public void initializeFactory() {
        factory = new KafkaReplicationProducerHolderFactory(
                mockedReplicatedServiceHolder,
                mockedObjectMapper,
                GIVEN_BOOTSTRAP_ADDRESS
        );
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void replicationProducersShouldBeCreated() {
        final FirstTestCRUDService firstGivenService = new FirstTestCRUDService();
        final SecondTestCRUDService secondGivenService = new SecondTestCRUDService();
        final List givenServices = List.of(firstGivenService, secondGivenService);

        when(mockedReplicatedServiceHolder.getServices()).thenReturn(givenServices);

        final KafkaReplicationProducerHolder actual = factory.create();
        final var actualProducersInfosByServices = findProducersByServices(actual)
                .entrySet()
                .stream()
                .collect(toMap(Entry::getKey, producerByService -> createProducerInfo(producerByService.getValue())));
        final var expectedProducersInfosByServices = Map.of(
                firstGivenService,
                createProducerInfo("first-topic", 10, 500, 100000),

                secondGivenService,
                createProducerInfo("second-topic", 15, 515, 110000)
        );
        assertEquals(expectedProducersInfosByServices, actualProducersInfosByServices);
    }

    @SuppressWarnings("unchecked")
    private static Map<AbsServiceRUD<?, ?, ?, ?, ?>, KafkaReplicationProducer<?, ?>> findProducersByServices(
            final KafkaReplicationProducerHolder holder
    ) {
        return getFieldValue(holder, FIELD_NAME_HOLDER_PRODUCERS_BY_SERVICES, Map.class);
    }

    @SuppressWarnings("SameParameterValue")
    private TestProducerInfo createProducerInfo(final String topicName,
                                                final int batchSize,
                                                final int lingerMs,
                                                final int deliveryTimeoutMs) {
        return TestProducerInfo.builder()
                .topicName(topicName)
                .objectMapper(mockedObjectMapper)
                .keySerializerType(GIVEN_KEY_SERIALIZER_TYPE)
                .batchSize(batchSize)
                .lingerMs(lingerMs)
                .deliveryTimeoutMs(deliveryTimeoutMs)
                .build();
    }

    private static TestProducerInfo createProducerInfo(final KafkaReplicationProducer<?, ?> producer) {
        return TestProducerInfo.builder()
                .topicName(getTopicName(producer))
                .objectMapper(getObjectMapper(producer))
                .keySerializerType(getKeySerializerType(producer))
                .batchSize(getBatchSize(producer))
                .lingerMs(getLingerMs(producer))
                .deliveryTimeoutMs(getDeliveryTimeout(producer))
                .build();
    }

    private static String getTopicName(final KafkaReplicationProducer<?, ?> producer) {
        return getFieldValue(producer, FIELD_NAME_PRODUCER_TOPIC_NAME, String.class);
    }

    private static ObjectMapper getObjectMapper(final KafkaReplicationProducer<?, ?> producer) {
        return getFieldValue(producer, FIELD_NAME_PRODUCER_OBJECT_MAPPER, ObjectMapper.class);
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Serializer<?>> getKeySerializerType(final KafkaReplicationProducer<?, ?> producer) {
        return getKafkaTemplateProperty(producer, KEY_SERIALIZER_CLASS_CONFIG, Class.class);
    }

    private static int getBatchSize(final KafkaReplicationProducer<?, ?> producer) {
        return getKafkaTemplateProperty(producer, BATCH_SIZE_CONFIG, Integer.class);
    }

    private static int getLingerMs(final KafkaReplicationProducer<?, ?> producer) {
        return getKafkaTemplateProperty(producer, LINGER_MS_CONFIG, Integer.class);
    }

    private static int getDeliveryTimeout(final KafkaReplicationProducer<?, ?> producer) {
        return getKafkaTemplateProperty(producer, DELIVERY_TIMEOUT_MS_CONFIG, Integer.class);
    }

    private static <P> P getKafkaTemplateProperty(final KafkaReplicationProducer<?, ?> producer,
                                                  final String propertyKey,
                                                  final Class<P> propertyType) {
        final Object value = getKafkaTemplate(producer)
                .getProducerFactory()
                .getConfigurationProperties()
                .get(propertyKey);
        return propertyType.cast(value);
    }

    private static KafkaTemplate<?, ?> getKafkaTemplate(final KafkaReplicationProducer<?, ?> producer) {
        return getFieldValue(producer, FIELD_NAME_PRODUCER_KAFKA_TEMPLATE, KafkaTemplate.class);
    }

    @Value
    @AllArgsConstructor
    @Builder
    private static class TestProducerInfo {
        ObjectMapper objectMapper;
        Class<? extends Serializer<?>> keySerializerType;
        int batchSize;
        int lingerMs;
        int deliveryTimeoutMs;
        String topicName;
    }
}
