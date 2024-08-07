package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

public final class ReplicationKafkaTemplateRegistry extends Registry<JpaRepository<?, ?>, KafkaTemplate<Object, ProducedReplication<?>>> {

    public ReplicationKafkaTemplateRegistry(Map<JpaRepository<?, ?>, KafkaTemplate<Object, ProducedReplication<?>>> valuesByKeys) {
        super(valuesByKeys);
    }
}
