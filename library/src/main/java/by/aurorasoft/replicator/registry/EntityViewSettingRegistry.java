package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting.EntityViewSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class EntityViewSettingRegistry {

    public Optional<EntityViewSetting[]> get(JpaRepository<?, ?> repository) {
        return Optional.empty();
    }
}
