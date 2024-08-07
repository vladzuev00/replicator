package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting.EntityViewSetting;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import org.springframework.kafka.core.KafkaTemplate;

import static by.aurorasoft.replicator.util.IdUtil.getId;
import static com.monitorjbl.json.Match.match;
import static java.util.Arrays.stream;

public final class SaveReplicationProducer extends ReplicationProducer<EntityJsonView<?>> {
    private final EntityViewSetting[] entityViewSettings;

    public SaveReplicationProducer(String topicName,
                                   KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate,
                                   EntityViewSetting[] entityViewSettings) {
        super(topicName, kafkaTemplate);
        this.entityViewSettings = entityViewSettings;
    }

    @Override
    protected Object getEntityId(Object entity) {
        return getId(entity);
    }

    @Override
    protected EntityJsonView<?> createReplicationBody(Object entity) {
        EntityJsonView<Object> entityJsonView = new EntityJsonView<>(entity);
        applyEntityViewSetting(entityJsonView);
        return entityJsonView;
    }

    @Override
    protected ProducedReplication<?> createReplication(EntityJsonView<?> entityJsonView) {
        return new SaveProducedReplication(entityJsonView);
    }

    private void applyEntityViewSetting(EntityJsonView<Object> entityJsonView) {
        stream(entityViewSettings).forEach(setting -> applyEntityViewSetting(entityJsonView, setting));
    }

    private void applyEntityViewSetting(EntityJsonView<Object> entityJsonView, EntityViewSetting setting) {
        entityJsonView.onClass(setting.getType(), match().exclude(setting.getExcludedFields()));
    }
}
