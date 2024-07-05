package by.aurorasoft.replicator.consuming.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class ConsumingSerdeTest {
    private static final Deserializer<Long> GIVEN_DESERIALIZER = new LongDeserializer();

    private final ConsumingSerde<Long> serde = new ConsumingSerde<>(GIVEN_DESERIALIZER);

    @Test(expected = UnsupportedOperationException.class)
    public void serializerShouldNotBeGot() {
        serde.serializer();
    }

    @Test
    public void deserializerShouldBeGot() {
        Deserializer<Long> actual = serde.deserializer();
        assertSame(GIVEN_DESERIALIZER, actual);
    }
}
