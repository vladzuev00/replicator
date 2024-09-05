package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.jupiter.api.Test;

import static by.aurorasoft.replicator.testutil.ProducerConfigUtil.createProducerConfig;
import static org.junit.jupiter.api.Assertions.*;

public final class ReplicationKafkaTemplateKeySerializerFactoryTest {
    private final ReplicationKafkaTemplateKeySerializerFactory factory = new ReplicationKafkaTemplateKeySerializerFactory();

    @Test
    public void serializerShouldBeCreated() {
        Class<? extends Serializer<?>> givenIdSerializerType = LongSerializer.class;
        ProducerConfig givenConfig = createProducerConfig(givenIdSerializerType);

        Serializer<Object> actual = factory.create(givenConfig);
        assertNotNull(actual);
        assertTrue(givenIdSerializerType.isInstance(actual));
    }

    @Test
    public void serializerShouldNotBeCreated() {
        Class<? extends Serializer<?>> givenIdSerializerType = SerializerWithoutDefaultConstructor.class;
        ProducerConfig givenConfig = createProducerConfig(givenIdSerializerType);

        assertThrows(NoSuchMethodException.class, () -> factory.create(givenConfig));
    }

    @RequiredArgsConstructor
    private static final class SerializerWithoutDefaultConstructor implements Serializer<Long> {

        @SuppressWarnings("unused")
        private final Object object;

        @Override
        public byte[] serialize(String topic, Long value) {
            throw new UnsupportedOperationException();
        }
    }
}
