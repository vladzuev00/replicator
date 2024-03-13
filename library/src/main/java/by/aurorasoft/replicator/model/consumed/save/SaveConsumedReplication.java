package by.aurorasoft.replicator.model.consumed.save;

import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class SaveConsumedReplication<ID, E extends AbstractEntity<ID>> implements ConsumedReplication<ID, E> {
    private final E entity;


}
