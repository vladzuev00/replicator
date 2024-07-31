package by.aurorasoft.replicator.model.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monitorjbl.json.JsonView;

public final class EntityJsonView<T> extends JsonView<T> {

    public EntityJsonView(T entity) {
        super(entity);
    }

    @JsonIgnore
    public T getEntity() {
        return value;
    }
}
