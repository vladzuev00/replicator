package by.aurorasoft.replicator.model.component;

import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
public final class ReplicationConsumer<E, ID> extends ReplicationComponent<E, ID> {
    private final Deserializer<ID> idDeserializer;
    private final TypeReference<ConsumedReplication<E, ID>> replicationTypeReference;

    public ReplicationConsumer(String topic,
                               JpaRepository<E, ID> repository,
                               Deserializer<ID> idDeserializer,
                               TypeReference<ConsumedReplication<E, ID>> replicationTypeReference) {
        super(topic, repository);
        this.idDeserializer = idDeserializer;
        this.replicationTypeReference = replicationTypeReference;
    }
}
