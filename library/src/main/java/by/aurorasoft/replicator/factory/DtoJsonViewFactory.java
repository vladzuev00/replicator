package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.View;
import by.aurorasoft.replicator.model.view.DtoJsonView;
import org.springframework.stereotype.Component;

import static com.monitorjbl.json.Match.match;
import static java.util.Arrays.stream;

@Component
public final class DtoJsonViewFactory {

    public <T> DtoJsonView<T> create(T dto, View[] configs) {
        DtoJsonView<T> view = new DtoJsonView<>(dto);
        stream(configs).forEach(config -> putMatch(view, config));
        return view;
    }

    private void putMatch(DtoJsonView<?> view, View config) {
//        view.onClass(config.type(), match().include(config.includedFields()).exclude(config.excludedFields()));
    }
}
