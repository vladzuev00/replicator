package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.producer.DeleteReplicationProducer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

public class DeleteReplicationProducerRegistry extends ReplicationProducerRegistry<DeleteReplicationProducer> {

    public DeleteReplicationProducerRegistry(Map<JpaRepository<?, ?>, DeleteReplicationProducer> producersByRepositories) {
        super(producersByRepositories);
    }

    public Optional<DeleteReplicationProducer> get(JpaRepository<?, ?> repository) {
        return Optional.empty();
    }
}
