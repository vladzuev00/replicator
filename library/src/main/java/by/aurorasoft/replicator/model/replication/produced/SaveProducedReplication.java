package by.aurorasoft.replicator.model.replication.produced;

import static by.aurorasoft.replicator.util.IdUtil.getId;

public final class SaveProducedReplication extends ProducedReplication {

    public SaveProducedReplication(final Object dto) {
        super(dto);
    }

    @Override
    protected Object getEntityId(final Object dto) {
        return getId(dto);
    }
}
