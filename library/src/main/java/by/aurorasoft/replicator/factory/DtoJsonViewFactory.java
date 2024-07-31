package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.EntityView;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import org.springframework.stereotype.Component;

import static java.util.Arrays.stream;

@Component
public final class DtoJsonViewFactory {

    public <T> EntityJsonView<T> create(T dto, EntityView[] configs) {
        EntityJsonView<T> view = new EntityJsonView<>(dto);
        stream(configs).forEach(config -> putMatch(view, config));
        return view;
    }

    private void putMatch(EntityJsonView<?> view, EntityView config) {
//        view.onClass(config.type(), match().include(config.includedFields()).exclude(config.excludedFields()));
    }
}
