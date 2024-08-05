package by.aurorasoft.replicator.model.config.component;

import by.aurorasoft.replicator.model.config.EntityViewConfig;
import by.aurorasoft.replicator.model.config.ProducerConfig;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
public final class ReplicationProducer<E, ID> extends ReplicationComponentConfig<E, ID> {
    private final ProducerConfig config;
    private final EntityViewConfig[] entityViewConfigs;

    public ReplicationProducer(String topic,
                               JpaRepository<E, ID> repository,
                               ProducerConfig config,
                               EntityViewConfig[] entityViewConfigs) {
        super(topic, repository);
        this.config = config;
        this.entityViewConfigs = entityViewConfigs;
    }
}
