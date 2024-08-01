package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.EntityView;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import org.springframework.stereotype.Component;

import static com.monitorjbl.json.Match.match;
import static java.util.Arrays.stream;

@Component
public final class EntityJsonViewFactory {

    public <T> EntityJsonView<T> create(T entity, EntityView[] configs) {
        EntityJsonView<T> view = new EntityJsonView<>(entity);
        stream(configs).forEach(config -> configure(view, config));
        return view;
    }

    private void configure(EntityJsonView<?> view, EntityView config) {
        view.onClass(config.type(), match().exclude(config.excludedFields()));
    }
}
