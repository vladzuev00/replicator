package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.model.view.DtoJsonView;
import com.monitorjbl.json.Match;
import org.springframework.stereotype.Component;

@Component
public final class DtoJsonViewFactory {

    public <T> DtoJsonView<T> create(T dto, DtoViewConfig config) {
        DtoJsonView<T> view = new DtoJsonView<>(dto);
        view.onClass(Match.match().)
    }
}
