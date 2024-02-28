//package by.aurorasoft.replicator.producer;
//
//import by.aurorasoft.replicator.annotation.ReplicatedService;
//import by.aurorasoft.replicator.config.ReplicationProducerConfig;
//import by.aurorasoft.replicator.holder.KafkaReplicationProducerHolder;
//import by.aurorasoft.replicator.holder.ReplicatedServiceHolder;
//import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
//import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
//import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
//import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Value;
//import org.apache.avro.Schema;
//import org.apache.kafka.common.serialization.LongSerializer;
//import org.apache.kafka.common.serialization.Serializer;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import java.lang.reflect.Field;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import static java.util.Objects.requireNonNull;
//import static java.util.stream.Collectors.toMap;
//import static org.apache.kafka.clients.producer.ProducerConfig.*;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.when;
//import static org.springframework.util.ReflectionUtils.findField;
//import static org.springframework.util.ReflectionUtils.getField;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class KafkaReplicationProducerHolderFactoryTest {
//    private static final String FIELD_NAME_HOLDER_PRODUCERS_BY_SERVICES = "producersByServices";
//
//    private static final String FIELD_NAME_PRODUCER_TOPIC_NAME = "topicName";
//    private static final String FIELD_NAME_PRODUCER_OBJECT_MAPPER = "objectMapper";
//    private static final String FIELD_NAME_PRODUCER_SCHEMA = "schema";
//    private static final String FIELD_NAME_PRODUCER_KAFKA_TEMPLATE = "kafkaTemplate";
//
//    private static final String GIVEN_BOOTSTRAP_ADDRESS = "127.0.0.1:9092";
//    private static final Class<? extends Serializer<?>> GIVEN_KEY_SERIALIZER_TYPE = LongSerializer.class;
//
//    @Mock
//    private ReplicatedServiceHolder mockedReplicatedServiceHolder;
//
//    @Mock
//    private ObjectMapper mockedObjectMapper;
//
//    @Mock
//    private ReplicationProducerConfig mockedProducerConfig;
//
//    @Mock
//    private Schema mockedSchema;
//
//    private KafkaReplicationProducerHolderFactory factory;
//
//    @Before
//    public void initializeFactory() {
//        factory = new KafkaReplicationProducerHolderFactory(
//                mockedReplicatedServiceHolder,
//                mockedObjectMapper,
//                mockedProducerConfig,
//                mockedSchema,
//                GIVEN_BOOTSTRAP_ADDRESS
//        );
//    }
//
//    @Test
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    public void replicationProducersShouldBeCreated() {
//        final TestFirstService firstGivenService = new TestFirstService();
//        final TestSecondService secondGivenService = new TestSecondService();
//        final TestThirdService thirdGivenService = new TestThirdService();
//        final List givenServices = List.of(firstGivenService, secondGivenService, thirdGivenService);
//
//        final int givenProducerBatchSize = 5;
//        final int givenProducerLingerMs = 10;
//        final int givenDeliveryTimeoutMs = 15;
//
//        when(mockedReplicatedServiceHolder.getServices()).thenReturn(givenServices);
//        when(mockedProducerConfig.getBatchSize()).thenReturn(givenProducerBatchSize);
//        when(mockedProducerConfig.getLingerMs()).thenReturn(givenProducerLingerMs);
//        when(mockedProducerConfig.getDeliveryTimeoutMs()).thenReturn(givenDeliveryTimeoutMs);
//
//        final KafkaReplicationProducerHolder actual = factory.create();
//        final var actualProducersInfosByServices = findProducersByServices(actual)
//                .entrySet()
//                .stream()
//                .collect(toMap(Entry::getKey, producerByService -> createProducerInfo(producerByService.getValue())));
//        final var expectedProducersInfosByServices = Map.of(
//                firstGivenService,
//                createProducerInfo("first-topic", givenProducerBatchSize, givenProducerLingerMs, givenDeliveryTimeoutMs),
//
//                secondGivenService,
//                createProducerInfo("second-topic", givenProducerBatchSize, givenProducerLingerMs, givenDeliveryTimeoutMs),
//
//                thirdGivenService,
//                createProducerInfo("third-topic", givenProducerBatchSize, givenProducerLingerMs, givenDeliveryTimeoutMs)
//        );
//        assertEquals(expectedProducersInfosByServices, actualProducersInfosByServices);
//    }
//
//    @SuppressWarnings("unchecked")
//    private static Map<AbsServiceRUD<?, ?, ?, ?, ?>, KafkaReplicationProducer<?, ?>> findProducersByServices(
//            final KafkaReplicationProducerHolder holder
//    ) {
//        return getFieldValue(holder, FIELD_NAME_HOLDER_PRODUCERS_BY_SERVICES, Map.class);
//    }
//
//    private static TestProducerInfo createProducerInfo(final KafkaReplicationProducer<?, ?> producer) {
//        return TestProducerInfo.builder()
//                .topicName(getTopicName(producer))
//                .objectMapper(getObjectMapper(producer))
//                .schema(getSchema(producer))
//                .keySerializerType(getKeySerializerType(producer))
//                .batchSize(getBatchSize(producer))
//                .lingerMs(getLingerMs(producer))
//                .deliveryTimeoutMs(getDeliveryTimeout(producer))
//                .build();
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private TestProducerInfo createProducerInfo(final String topicName,
//                                                final int batchSize,
//                                                final int lingerMs,
//                                                final int deliveryTimeoutMs) {
//        return TestProducerInfo.builder()
//                .topicName(topicName)
//                .objectMapper(mockedObjectMapper)
//                .schema(mockedSchema)
//                .keySerializerType(GIVEN_KEY_SERIALIZER_TYPE)
//                .batchSize(batchSize)
//                .lingerMs(lingerMs)
//                .deliveryTimeoutMs(deliveryTimeoutMs)
//                .build();
//    }
//
//    private static String getTopicName(final KafkaReplicationProducer<?, ?> producer) {
//        return getFieldValue(producer, FIELD_NAME_PRODUCER_TOPIC_NAME, String.class);
//    }
//
//    private static ObjectMapper getObjectMapper(final KafkaReplicationProducer<?, ?> producer) {
//        return getFieldValue(producer, FIELD_NAME_PRODUCER_OBJECT_MAPPER, ObjectMapper.class);
//    }
//
//    private static Schema getSchema(final KafkaReplicationProducer<?, ?> producer) {
//        return getFieldValue(producer, FIELD_NAME_PRODUCER_SCHEMA, Schema.class);
//    }
//
//    @SuppressWarnings("unchecked")
//    private static Class<? extends Serializer<?>> getKeySerializerType(final KafkaReplicationProducer<?, ?> producer) {
//        return getKafkaTemplateProperty(producer, KEY_SERIALIZER_CLASS_CONFIG, Class.class);
//    }
//
//    private static int getBatchSize(final KafkaReplicationProducer<?, ?> producer) {
//        return getKafkaTemplateProperty(producer, BATCH_SIZE_CONFIG, Integer.class);
//    }
//
//    private static int getLingerMs(final KafkaReplicationProducer<?, ?> producer) {
//        return getKafkaTemplateProperty(producer, LINGER_MS_CONFIG, Integer.class);
//    }
//
//    private static int getDeliveryTimeout(final KafkaReplicationProducer<?, ?> producer) {
//        return getKafkaTemplateProperty(producer, DELIVERY_TIMEOUT_MS_CONFIG, Integer.class);
//    }
//
//    private static <P> P getKafkaTemplateProperty(final KafkaReplicationProducer<?, ?> producer,
//                                                  final String propertyKey,
//                                                  final Class<P> propertyType) {
//        final Object value = getKafkaTemplate(producer)
//                .getProducerFactory()
//                .getConfigurationProperties()
//                .get(propertyKey);
//        return propertyType.cast(value);
//    }
//
//    private static KafkaTemplate<?, ?> getKafkaTemplate(final KafkaReplicationProducer<?, ?> producer) {
//        return getFieldValue(producer, FIELD_NAME_PRODUCER_KAFKA_TEMPLATE, KafkaTemplate.class);
//    }
//
//    private static <P, T> P getFieldValue(final T target, final String fieldName, final Class<P> valueType) {
//        final Field field = requireNonNull(findField(target.getClass(), fieldName));
//        field.setAccessible(true);
//        try {
//            final Object value = getField(field, target);
//            return valueType.cast(value);
//        } finally {
//            field.setAccessible(false);
//        }
//    }
//
//    @ReplicatedService(topicName = "first-topic", idSerializer = LongSerializer.class)
//    static class TestFirstService extends AbsServiceRUD<
//            Long,
//            AbstractEntity<Long>,
//            AbstractDto<Long>,
//            AbsMapperEntityDto<AbstractEntity<Long>, AbstractDto<Long>>,
//            JpaRepository<AbstractEntity<Long>, Long>
//            > {
//
//        public TestFirstService() {
//            super(null, null);
//        }
//    }
//
//    @ReplicatedService(topicName = "second-topic", idSerializer = LongSerializer.class)
//    static class TestSecondService extends AbsServiceRUD<
//            Long,
//            AbstractEntity<Long>,
//            AbstractDto<Long>,
//            AbsMapperEntityDto<AbstractEntity<Long>, AbstractDto<Long>>,
//            JpaRepository<AbstractEntity<Long>, Long>
//            > {
//
//        public TestSecondService() {
//            super(null, null);
//        }
//    }
//
//    @ReplicatedService(topicName = "third-topic", idSerializer = LongSerializer.class)
//    static class TestThirdService extends AbsServiceRUD<
//            Long,
//            AbstractEntity<Long>,
//            AbstractDto<Long>,
//            AbsMapperEntityDto<AbstractEntity<Long>, AbstractDto<Long>>,
//            JpaRepository<AbstractEntity<Long>, Long>
//            > {
//
//        public TestThirdService() {
//            super(null, null);
//        }
//    }
//
//    @Value
//    @AllArgsConstructor
//    @Builder
//    private static class TestProducerInfo {
//        String topicName;
//        ObjectMapper objectMapper;
//        Schema schema;
//        Class<? extends Serializer<?>> keySerializerType;
//        int batchSize;
//        int lingerMs;
//        int deliveryTimeoutMs;
//    }
//}
