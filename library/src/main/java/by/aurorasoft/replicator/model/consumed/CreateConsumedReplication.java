package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import lombok.Value;
import org.springframework.data.jpa.repository.JpaRepository;

@Value
public class CreateConsumedReplication<ID, ENTITY extends AbstractEntity<ID>> implements ConsumedReplication<ID, ENTITY> {
    ENTITY entity;

    @Override
    public void execute(final JpaRepository<ENTITY, ID> repository) {
        repository.save(entity);
    }
}
