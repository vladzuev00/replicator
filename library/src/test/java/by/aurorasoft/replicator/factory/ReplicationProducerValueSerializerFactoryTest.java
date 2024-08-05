package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.mapperwrapper.ProducedReplicationMapperWrapper;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerValueSerializerFactoryTest {
    private static final String FIELD_NAME_OBJECT_MAPPER = "objectMapper";

    @Mock
    private ProducedReplicationMapperWrapper mockedMapperWrapper;

    private ReplicationProducerValueSerializerFactory factory;

    @BeforeEach
    public void initializeFactory() {
        factory = new ReplicationProducerValueSerializerFactory(mockedMapperWrapper);
    }

    @Test
    public void serializerShouldBeCreated() {
        ObjectMapper givenObjectMapper = mock(ObjectMapper.class);
        when(mockedMapperWrapper.getMapper()).thenReturn(givenObjectMapper);

        Serializer<ProducedReplication<?>> actual = factory.create();
        assertTrue(actual instanceof JsonSerializer);

        ObjectMapper actualObjectMapper = getObjectMapper(((JsonSerializer<?>) actual));
        assertSame(givenObjectMapper, actualObjectMapper);
    }

    private ObjectMapper getObjectMapper(JsonSerializer<?> serializer) {
        return getFieldValue(serializer, FIELD_NAME_OBJECT_MAPPER, ObjectMapper.class);
    }
}
