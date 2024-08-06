package by.aurorasoft.replicator.consuming.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ConsumingSerdeTest {
    private final Deserializer<Long> givenDeserializer = new LongDeserializer();
    private final ConsumingSerde<Long> serde = new ConsumingSerde<>(givenDeserializer);

    @Test
    public void serializerShouldNotBeGot() {
        assertThrows(UnsupportedOperationException.class, serde::serializer);
    }

    @Test
    public void deserializerShouldBeGot() {
        Deserializer<Long> actual = serde.deserializer();
        assertSame(givenDeserializer, actual);
    }
}
