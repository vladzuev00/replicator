package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.model.view.DtoJsonView;

import static by.aurorasoft.replicator.util.IdUtil.getId;

public final class SaveProducedReplication extends ProducedReplication<DtoJsonView<?>> {

    public SaveProducedReplication(DtoJsonView<?> dtoJsonView) {
        super(dtoJsonView);
    }

    @Override
    protected Object getEntityId(DtoJsonView<?> dtoJsonView) {
        return getId(dtoJsonView.getDto());
    }
}
