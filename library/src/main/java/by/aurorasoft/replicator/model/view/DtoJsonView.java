package by.aurorasoft.replicator.model.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monitorjbl.json.JsonView;

public final class DtoJsonView<T> extends JsonView<T> {

    public DtoJsonView(T entity) {
        super(entity);
    }

    @JsonIgnore
    public T getDto() {
        return value;
    }
}
