package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumedReplication<ID, E extends AbstractEntity<ID>> {
    void execute(final JpaRepository<E, ID> repository);
}
