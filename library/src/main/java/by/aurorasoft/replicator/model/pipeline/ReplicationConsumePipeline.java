package by.aurorasoft.replicator.model.pipeline;

import by.aurorasoft.replicator.consuming.serde.ConsumedReplicationSerde;
import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import by.aurorasoft.replicator.property.ReplicationConsumeProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Objects;

@Getter
public final class ReplicationConsumePipeline<E, ID> {
    private final String id;
    private final String topic;
    private final Serde<ID> idSerde;
    private final Serde<ConsumedReplication<E, ID>> replicationSerde;
    private final JpaRepository<E, ID> repository;

    @Builder
    public ReplicationConsumePipeline(ReplicationConsumeProperty property,
                                      Serde<ID> idSerde,
                                      TypeReference<ConsumedReplication<E, ID>> replicationTypeReference,
                                      JpaRepository<E, ID> repository) {
        requireNonNull(property);
        id = requireNonNull(property.getPipelineId());
        topic = requireNonNull(property.getTopicName());
        this.idSerde = requireNonNull(idSerde);
        replicationSerde = new ConsumedReplicationSerde<>(requireNonNull(replicationTypeReference));
        this.repository = requireNonNull(repository);
    }

    private <T> T requireNonNull(T object) {
        return Objects.requireNonNull(object, "All properties of pipeline should be defined");
    }
}
