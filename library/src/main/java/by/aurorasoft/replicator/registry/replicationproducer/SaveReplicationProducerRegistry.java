package by.aurorasoft.replicator.registry.replicationproducer;

import by.aurorasoft.replicator.producer.SaveReplicationProducer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public final class SaveReplicationProducerRegistry extends ReplicationProducerRegistry<SaveReplicationProducer> {

    public SaveReplicationProducerRegistry(Map<JpaRepository<?, ?>, SaveReplicationProducer> producersByRepositories) {
        super(producersByRepositories);
    }
}
