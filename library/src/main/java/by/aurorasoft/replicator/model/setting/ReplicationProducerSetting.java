package by.aurorasoft.replicator.model.setting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
public final class ReplicationProducerSetting<E, ID> extends ReplicationComponentSetting<E, ID> {
    private final Class<? extends Serializer<?>> idSerializer;
    private final int batchSize;
    private final int lingerMs;
    private final int deliveryTimeoutMs;
    private final EntityViewSetting[] entityViewSettings;

    public ReplicationProducerSetting(String topic,
                                      JpaRepository<E, ID> repository,
                                      Class<? extends Serializer<?>> idSerializer,
                                      int batchSize,
                                      int lingerMs,
                                      int deliveryTimeoutMs,
                                      EntityViewSetting[] entityViewSettings) {
        super(topic, repository);
        this.idSerializer = idSerializer;
        this.batchSize = batchSize;
        this.lingerMs = lingerMs;
        this.deliveryTimeoutMs = deliveryTimeoutMs;
        this.entityViewSettings = entityViewSettings;
    }

    @RequiredArgsConstructor
    @Getter
    public final static class EntityViewSetting {
        private final Class<?> type;
        private final String[] excludedFields;
    }
}
