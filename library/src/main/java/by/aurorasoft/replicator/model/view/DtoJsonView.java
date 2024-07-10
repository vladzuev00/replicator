package by.aurorasoft.replicator.model.view;

import com.monitorjbl.json.JsonView;

public final class DtoJsonView<T> extends JsonView<T> {

    public DtoJsonView(T dto) {
        super(dto);
    }

    public T getDto() {
        return value;
    }
}
