package by.aurorasoft.replicator.factory.producedreplication;

import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public final class SaveProducedReplicationFactory {

    public SaveProducedReplication create(Object entity, JpaRepository<?, ?> repository) {
        throw new UnsupportedOperationException();
    }
}
