package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.producer.DeleteReplicationProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class DeleteReplicationProducerRegistry extends ReplicationProducerRegistry<DeleteReplicationProducer> {

    public DeleteReplicationProducerRegistry() {
        super(Collections.emptyMap());
    }

    public Optional<DeleteReplicationProducer> get(JpaRepository<?, ?> repository) {
        return Optional.empty();
    }
}
