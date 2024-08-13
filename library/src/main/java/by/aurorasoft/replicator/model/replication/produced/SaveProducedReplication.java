package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.model.view.EntityJsonView;

import static by.aurorasoft.replicator.util.IdUtil.getId;

public final class SaveProducedReplication extends ProducedReplication<EntityJsonView<?>> {

    public SaveProducedReplication(EntityJsonView<?> entityJsonView) {
        super(entityJsonView);
    }

    @Override
    protected Object getEntityIdInternal(EntityJsonView<?> body) {
        return getId(body.getEntity());
    }
}
