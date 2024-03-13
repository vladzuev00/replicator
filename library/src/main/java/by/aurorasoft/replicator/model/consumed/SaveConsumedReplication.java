package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

@RequiredArgsConstructor
@Getter
public class SaveConsumedReplication<ID, E extends AbstractEntity<ID>> implements ConsumedReplication<ID, E> {
    private final E entity;

    @Override
    public void execute(final JpaRepository<E, ID> repository) {
        repository.save(entity);
    }
}
