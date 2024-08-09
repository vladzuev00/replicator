package by.aurorasoft.replicator.model.setting;

import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.data.jpa.repository.JpaRepository;

import static java.util.Objects.requireNonNull;

@Getter
public final class ReplicationConsumeSetting<E, ID> extends ReplicationSetting<E, ID> {
    private final Deserializer<ID> idDeserializer;
    private final TypeReference<ConsumedReplication<E, ID>> replicationTypeReference;

    @Builder
    public ReplicationConsumeSetting(String topic,
                                     JpaRepository<E, ID> repository,
                                     Deserializer<ID> idDeserializer,
                                     TypeReference<ConsumedReplication<E, ID>> replicationTypeReference) {
        super(topic, repository);
        this.idDeserializer = requireNonNull(idDeserializer);
        this.replicationTypeReference = requireNonNull(replicationTypeReference);
    }
}