package by.aurorasoft.replicator.model.pipeline;

import by.aurorasoft.replicator.consuming.serde.ReplicationSerde;
import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Objects;

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
        this.id = requireNonNull(id);
        this.topic = requireNonNull(topic);
        this.idSerde = requireNonNull(idSerde);
        replicationSerde = new ReplicationSerde<>(requireNonNull(replicationTypeReference));
        this.repository = requireNonNull(repository);
    }

    private static <T> T requireNonNull(final T object) {
        return Objects.requireNonNull(object, "All properties of pipeline should be defined");
    }
}