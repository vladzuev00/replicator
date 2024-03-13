package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumedReplication<ID, ENTITY extends AbstractEntity<ID>> {
    void execute(final JpaRepository<ENTITY, ID> repository);
}
