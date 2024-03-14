//package by.aurorasoft.replicator.factory;
//
//import by.aurorasoft.replicator.base.dto.TestDto;
//import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumer;
//import by.aurorasoft.replicator.consuming.deserializer.ReplicationDeserializer;
//import by.aurorasoft.replicator.model.Replication;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//
//import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
//import static com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertSame;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ReplicationDeserializerFactoryTest {
//    private static final String FIELD_NAME_OBJECT_MAPPER = "objectMapper";
//    private static final String FIELD_NAME_TARGET_TYPE = "targetType";
//
//    @Mock
//    private ObjectMapper mockedObjectMapper;
//
//    private ReplicationDeserializerFactory factory;
//
//    @Before
//    public void initializeFactory() {
//        factory = new ReplicationDeserializerFactory(mockedObjectMapper);
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void deserializeShouldBeCreated() {
//        final ReplicationConsumer<Long, TestDto> givenConfig = mock(ReplicationConsumer.class);
//
//        final TypeReference<Replication<Long, TestDto>> givenTypeReference = new TypeReference<>() {
//        };
//        when(givenConfig.getReplicationTypeReference()).thenReturn(givenTypeReference);
//
//        final ReplicationDeserializer<Long, TestDto> actual = factory.create(givenConfig);
//
//        final ObjectMapper actualObjectMapper = getObjectMapper(actual);
//        assertSame(mockedObjectMapper, actualObjectMapper);
//
//        final JavaType actualTargetType = getTargetType(actual);
//        final JavaType expectedTargetType = defaultInstance()
//                .constructType(
//                        new TypeReference<Replication<Long, TestDto>>() {
//                        }
//                );
//        assertEquals(expectedTargetType, actualTargetType);
//    }
//
//    private static ObjectMapper getObjectMapper(final JsonDeserializer<?> deserializer) {
//        return getFieldValue(deserializer, FIELD_NAME_OBJECT_MAPPER, ObjectMapper.class);
//    }
//
//    private static JavaType getTargetType(final JsonDeserializer<?> deserializer) {
//        return getFieldValue(deserializer, FIELD_NAME_TARGET_TYPE, JavaType.class);
//    }
//}
