package by.aurorasoft.replicator.registry.replicationproducer;

import by.aurorasoft.replicator.producer.DeleteReplicationProducer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public final class DeleteReplicationProducerRegistry extends ReplicationProducerRegistry<DeleteReplicationProducer> {

    public DeleteReplicationProducerRegistry(Map<JpaRepository<?, ?>, DeleteReplicationProducer> producersByRepositories) {
        super(producersByRepositories);
    }
}