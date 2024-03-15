package by.aurorasoft.replicator.consuming;

import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.support.serializer.JsonSerde;

@Getter
public final class ReplicationConsumingPipeline<ID, E extends AbstractEntity<ID>> {
    private final String topic;
    private final Serde<ID> idSerde;
    private final JpaRepository<E, ID> repository;
    private final Serde<ConsumedReplication<ID, E>> replicationSerde;

    @Builder
    public ReplicationConsumingPipeline(final String topic,
                                        final Serde<ID> idSerde,
                                        final JpaRepository<E, ID> repository,
                                        final TypeReference<ConsumedReplication<ID, E>> replicationTypeReference) {
        this.topic = topic;
        this.idSerde = idSerde;
        this.repository = repository;
        this.replicationSerde = new JsonSerde<>(replicationTypeReference);
    }
}
