package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ReplicationKafkaTemplateKeySerializerFactoryTest {
    private final ReplicationKafkaTemplateKeySerializerFactory factory = new ReplicationKafkaTemplateKeySerializerFactory();

    @Test
    public void serializerShouldBeCreated() {
        Class<? extends Serializer<?>> givenIdSerializer = LongSerializer.class;
        ReplicationProducerSetting<?, ?> givenSetting = ReplicationProducerSetting.builder()
                .idSerializer(givenIdSerializer)
                .build();

        Serializer<Object> actual = factory.create(givenSetting);
        assertNotNull(actual);
        assertTrue(givenIdSerializer.isInstance(actual));
    }
}
