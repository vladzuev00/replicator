package by.aurorasoft.replicator.consuming;

import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Getter
public final class ReplicationConsumingPipelineConfig<ID, E extends AbstractEntity<ID>> {
    private final String topic;
    private final Serde<ID> idSerde;
    private final Serde<ConsumedReplication<ID, E>> replicationSerde;
    private final JpaRepository<E, ID> repository;

    @Builder
    public ReplicationConsumingPipelineConfig(final String topic,
                                              final Serde<ID> idSerde,
                                              final TypeReference<ConsumedReplication<ID, E>> replicationTypeReference,
                                              final JpaRepository<E, ID> repository) {
        this.topic = topic;
        this.idSerde = idSerde;
        this.replicationSerde = new JsonSerde<>(new JsonSerializer<>(), new JsonDeserializer<>(replicationTypeReference, false));
        this.repository = repository;
    }
}
