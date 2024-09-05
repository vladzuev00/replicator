package by.aurorasoft.replicator.factory.replication;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import org.springframework.stereotype.Component;

import static java.util.Arrays.stream;

@Component
public final class SaveProducedReplicationFactory {

    public SaveProducedReplication create(Object savedEntity, ReplicatedService.DtoViewConfig[] dtoViewConfigs) {
        return null;
//        EntityJsonView<?> view = new EntityJsonView<>(savedEntity);
//        applySettings(view, viewSettings);
//        return new SaveProducedReplication(view);
    }

//    private void applySettings(EntityJsonView<?> view, EntityViewSetting[] settings) {
//        stream(settings).forEach(setting -> applySetting(view, setting));
//    }
//
//    private void applySetting(EntityJsonView<?> view, EntityViewSetting setting) {
//        view.onClass(setting.getType(), match().exclude(setting.getExcludedFields()));
//    }
}
