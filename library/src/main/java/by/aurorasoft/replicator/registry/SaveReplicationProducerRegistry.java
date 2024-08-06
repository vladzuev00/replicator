package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.producer.SaveReplicationProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public final class SaveReplicationProducerRegistry extends ReplicationProducerRegistry<SaveReplicationProducer> {

    public SaveReplicationProducerRegistry() {
        super(Collections.emptyMap());
    }

    public Optional<SaveReplicationProducer> get(JpaRepository<?, ?> repository) {
        return Optional.empty();
    }
}
