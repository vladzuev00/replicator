package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

public final class KafkaTemplateRegistry extends Registry<JpaRepository<?, ?>, KafkaTemplate<Object, ProducedReplication<?>>> {

    public KafkaTemplateRegistry(Map<JpaRepository<?, ?>, KafkaTemplate<Object, ProducedReplication<?>>> valuesByKeys) {
        super(valuesByKeys);
    }
}
