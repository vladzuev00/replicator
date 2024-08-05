package by.aurorasoft.replicator.model.component;

import by.aurorasoft.replicator.model.config.EntityViewConfig;
import by.aurorasoft.replicator.model.config.ProducerConfig;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
public final class ReplicationProducer<E, ID> extends ReplicationComponent<E, ID> {
    private final ProducerConfig producerConfig;
    private final EntityViewConfig[] entityViewConfigs;

    public ReplicationProducer(String topic,
                               JpaRepository<E, ID> repository,
                               ProducerConfig producerConfig,
                               EntityViewConfig[] entityViewConfigs) {
        super(topic, repository);
        this.producerConfig = producerConfig;
        this.entityViewConfigs = entityViewConfigs;
    }
}
