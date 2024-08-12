package by.aurorasoft.replicator.factory.replication;

import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting.EntityViewSetting;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import org.springframework.stereotype.Component;

import static com.monitorjbl.json.Match.match;
import static java.util.Arrays.stream;

@Component
public final class SaveProducedReplicationFactory {

    public SaveProducedReplication create(Object savedEntity, EntityViewSetting[] entityViewSettings) {
        EntityJsonView<?> entityJsonView = new EntityJsonView<>(savedEntity);
        applyEntityViewSetting(entityJsonView, entityViewSettings);
        return new SaveProducedReplication(entityJsonView);
    }

    private void applyEntityViewSetting(EntityJsonView<?> view, EntityViewSetting[] entityViewSettings) {
        stream(entityViewSettings).forEach(setting -> applyEntityViewSetting(view, setting));
    }

    private void applyEntityViewSetting(EntityJsonView<?> view, EntityViewSetting setting) {
        view.onClass(setting.getType(), match().exclude(setting.getExcludedFields()));
    }
}
