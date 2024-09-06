package by.aurorasoft.replicator.factory.replication;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.view.DtoJsonView;
import org.springframework.stereotype.Component;

import static com.monitorjbl.json.Match.match;
import static java.util.Arrays.stream;

@Component
public final class SaveProducedReplicationFactory {

    public SaveProducedReplication create(Object saveDto, DtoViewConfig[] dtoViewConfigs) {
        DtoJsonView<?> dtoJsonView = new DtoJsonView<>(saveDto);
        applyConfigs(dtoJsonView, dtoViewConfigs);
        return new SaveProducedReplication(dtoJsonView);
    }

    private void applyConfigs(DtoJsonView<?> view, DtoViewConfig[] configs) {
        stream(configs).forEach(config -> applyConfig(view, config));
    }

    private void applyConfig(DtoJsonView<?> view, DtoViewConfig config) {
        view.onClass(config.type(), match().include(config.includedFields()).exclude(config.excludedFields()));
    }
}
