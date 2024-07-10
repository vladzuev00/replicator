package by.aurorasoft.replicator.model.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monitorjbl.json.JsonView;

public final class DtoJsonView<T> extends JsonView<T> {

    public DtoJsonView(T dto) {
        super(dto);
    }

    @JsonIgnore
    public T getDto() {
        return value;
    }
}
