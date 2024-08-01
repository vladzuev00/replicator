package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerValueSerializerFactory {
    private final ObjectMapper objectMapper;

    public JsonSerializer<ProducedReplication<?>> create() {
        return new JsonSerializer<>(objectMapper);
    }
}
