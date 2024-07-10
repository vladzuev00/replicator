package by.aurorasoft.replicator.model.replication.produced;

import com.monitorjbl.json.JsonView;

import static by.aurorasoft.replicator.util.IdUtil.getId;

public final class SaveProducedReplication extends ProducedReplication<JsonView<?>> {

    //TODO: JsonView instead of dto
    public SaveProducedReplication(JsonView<?> view) {
        super(view);
    }

    @Override
    protected Object getEntityId(JsonView<?> view) {
        view.
        return getId(dto);
    }
}
