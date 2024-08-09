package by.aurorasoft.replicator.model.setting;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.data.jpa.repository.JpaRepository;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

@Getter
public final class ReplicationProduceSetting<E, ID> extends ReplicationSetting<E, ID> {
    static final Integer DEFAULT_BATCH_SIZE = 10;
    static final Integer DEFAULT_LINGER_MS = 500;
    static final Integer DEFAULT_DELIVERY_TIMEOUT_MS = 100000;
    static final EntityViewSetting[] DEFAULT_ENTITY_VIEW_SETTINGS = {};

    private final Serializer<ID> idSerializer;
    private final int batchSize;
    private final int lingerMs;
    private final int deliveryTimeoutMs;
    private final EntityViewSetting[] entityViewSettings;

    @Builder
    public ReplicationProduceSetting(String topic,
                                     JpaRepository<E, ID> repository,
                                     Serializer<ID> idSerializer,
                                     Integer batchSize,
                                     Integer lingerMs,
                                     Integer deliveryTimeoutMs,
                                     EntityViewSetting[] entityViewSettings) {
        super(topic, repository);
        this.idSerializer = requireNonNull(idSerializer);
        this.batchSize = requireNonNullElse(batchSize, DEFAULT_BATCH_SIZE);
        this.lingerMs = requireNonNullElse(lingerMs, DEFAULT_LINGER_MS);
        this.deliveryTimeoutMs = requireNonNullElse(deliveryTimeoutMs, DEFAULT_DELIVERY_TIMEOUT_MS);
        this.entityViewSettings = requireNonNullElse(entityViewSettings, DEFAULT_ENTITY_VIEW_SETTINGS);
    }

    @RequiredArgsConstructor
    @Getter
    public final static class EntityViewSetting {
        private final Class<?> type;
        private final String[] excludedFields;
    }
}
