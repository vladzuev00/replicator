package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting.EntityViewSetting;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import by.aurorasoft.replicator.transactioncallback.SaveReplicationTransactionCallback;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import static by.aurorasoft.replicator.util.IdUtil.getId;
import static com.monitorjbl.json.Match.match;
import static java.util.Arrays.stream;
import static org.springframework.transaction.support.TransactionSynchronizationManager.isActualTransactionActive;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

@RequiredArgsConstructor
@Getter
public final class ReplicationProducer {
    private final KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate;
    private final String topic;
    private final EntityViewSetting[] entityViewSettings;

    public void produceSaveAfterCommit(Object savedEntity) {
        SaveProducedReplication replication = createSaveReplication(savedEntity);
        produceAfterCommit(getId(savedEntity), replication);
    }

    public void produceDeleteAfterCommit(Object entityId) {
        DeleteProducedReplication replication = new DeleteProducedReplication(entityId);
        produceAfterCommit(entityId, replication);
    }

    private SaveProducedReplication createSaveReplication(Object savedEntity) {
        EntityJsonView<?> entityJsonView = new EntityJsonView<>(savedEntity);
        applyEntityViewSetting(entityJsonView);
        return new SaveProducedReplication(entityJsonView);
    }

    private void applyEntityViewSetting(EntityJsonView<?> view) {
        stream(entityViewSettings).forEach(setting -> applyEntityViewSetting(view, setting));
    }

    private void applyEntityViewSetting(EntityJsonView<?> view, EntityViewSetting setting) {
        view.onClass(setting.getType(), match().exclude(setting.getExcludedFields()));
    }

    private void produceAfterCommit(Object entityId, ProducedReplication<?> replication) {
        ProducerRecord<Object, ProducedReplication<?>> record = new ProducerRecord<>(topic, entityId, replication);
        if (isActualTransactionActive()) {
//            registerSynchronization(new SaveReplicationTransactionCallback());
        }
        kafkaTemplate.send(record);
    }
}
