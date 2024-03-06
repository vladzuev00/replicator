package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumerConfig;
import by.aurorasoft.replicator.deserializer.ReplicationDeserializer;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationDeserializerFactory {
    private final ObjectMapper objectMapper;

    public <ID, DTO extends AbstractDto<ID>> ReplicationDeserializer<ID, DTO> createDeserializer(
            final ReplicationConsumerConfig<ID, DTO> config
    ) {
        return new ReplicationDeserializer<>(config.getReplicationTypeReference(), objectMapper);
    }
}
