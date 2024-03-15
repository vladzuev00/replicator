package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumingPipelineStarter;
import by.aurorasoft.replicator.consuming.deserializer.ReplicationDeserializer;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationDeserializerFactory {
    private final ObjectMapper objectMapper;

    public <ID, E extends AbstractEntity<ID>> ReplicationDeserializer<ID, E> create(
            final ReplicationConsumingPipelineStarter<ID, E> consumer
    ) {
        return new ReplicationDeserializer<>(objectMapper, consumer.getReplicationTypeReference());
    }
}
