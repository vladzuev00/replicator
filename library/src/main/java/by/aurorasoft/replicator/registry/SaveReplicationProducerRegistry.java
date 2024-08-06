package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.producer.SaveReplicationProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

public final class SaveReplicationProducerRegistry extends ReplicationProducerRegistry<SaveReplicationProducer> {

    public SaveReplicationProducerRegistry(Map<JpaRepository<?, ?>, SaveReplicationProducer> producersByRepositories) {
        super(producersByRepositories);
    }

    public Optional<SaveReplicationProducer> get(JpaRepository<?, ?> repository) {
        return Optional.empty();
    }
}
