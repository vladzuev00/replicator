package by.aurorasoft.replicator.registry.replicationproducer;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.Registry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public abstract class ReplicationProducerRegistry<P extends ReplicationProducer<?>> extends Registry<JpaRepository<?, ?>, P> {

    public ReplicationProducerRegistry(Map<JpaRepository<?, ?>, P> producersByRepositories) {
        super(producersByRepositories);
    }
}
