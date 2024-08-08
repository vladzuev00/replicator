package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.model.view.EntityJsonView;

public final class SaveProducedReplication extends ProducedReplication<EntityJsonView<?>> {

    public SaveProducedReplication(EntityJsonView<?> entityJsonView) {
        super(entityJsonView);
    }
}
