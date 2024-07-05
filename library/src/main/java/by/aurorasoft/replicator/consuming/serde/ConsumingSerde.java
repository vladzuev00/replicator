package by.aurorasoft.replicator.consuming.serde;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

@RequiredArgsConstructor
public final class ConsumingSerde<T> implements Serde<T> {
    private final Deserializer<T> deserializer;

    @Override
    public Serializer<T> serializer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Deserializer<T> deserializer() {
        return deserializer;
    }
}
