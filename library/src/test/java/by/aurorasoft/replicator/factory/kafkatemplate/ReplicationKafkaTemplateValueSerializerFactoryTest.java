package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.serializer.JsonSerializer;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public final class ReplicationKafkaTemplateValueSerializerFactoryTest {
    private static final String FIELD_NAME_OBJECT_MAPPER = "objectMapper";

    @Mock
    private ObjectMapper mockedObjectMapper;

    private ReplicationKafkaTemplateValueSerializerFactory factory;

    @BeforeEach
    public void initializeFactory() {
        factory = new ReplicationKafkaTemplateValueSerializerFactory(mockedObjectMapper);
    }

    @Test
    public void serializerShouldBeCreated() {
        Serializer<ProducedReplication<?>> actual = factory.create();
        assertTrue(actual instanceof JsonSerializer);

        ObjectMapper actualObjectMapper = getObjectMapper(((JsonSerializer<?>) actual));
        assertSame(mockedObjectMapper, actualObjectMapper);
    }

    private ObjectMapper getObjectMapper(JsonSerializer<?> serializer) {
        return getFieldValue(serializer, FIELD_NAME_OBJECT_MAPPER, ObjectMapper.class);
    }
}
