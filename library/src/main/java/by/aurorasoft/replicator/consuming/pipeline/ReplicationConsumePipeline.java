package by.aurorasoft.replicator.consuming.pipeline;

import by.aurorasoft.replicator.consuming.serde.ReplicationSerde;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
public final class ReplicationConsumePipeline<ID, E extends AbstractEntity<ID>> {
    private final String id;
    private final String topic;
    private final Serde<ID> idSerde;
    private final Serde<ConsumedReplication<ID, E>> replicationSerde;
    private final JpaRepository<E, ID> repository;

    @Builder
    public ReplicationConsumePipeline(final String id,
                                      final String topic,
                                      final Serde<ID> idSerde,
                                      final TypeReference<ConsumedReplication<ID, E>> replicationTypeReference,
                                      final JpaRepository<E, ID> repository) {
        this.id = id;
        this.topic = topic;
        this.idSerde = idSerde;
        replicationSerde = new ReplicationSerde<>(replicationTypeReference);
        this.repository = repository;
    }
}
