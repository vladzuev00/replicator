package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting.EntityViewSetting;
import by.aurorasoft.replicator.transaction.callback.ReplicationTransactionCallback;
import by.aurorasoft.replicator.transaction.manager.ReplicationTransactionManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import static by.aurorasoft.replicator.util.IdUtil.getId;

@RequiredArgsConstructor
@Getter
public final class ReplicationProducer {
    private final SaveProducedReplicationFactory saveReplicationFactory;
    private final KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate;
    private final ReplicationTransactionManager transactionManager;
    private final String topic;
    private final EntityViewSetting[] entityViewSettings;

    public void produceSaveAfterCommit(Object savedEntity) {
        SaveProducedReplication replication = saveReplicationFactory.create(savedEntity, entityViewSettings);
        produceAfterCommit(getId(savedEntity), replication);
    }

    public void produceDeleteAfterCommit(Object entityId) {
        DeleteProducedReplication replication = new DeleteProducedReplication(entityId);
        produceAfterCommit(entityId, replication);
    }

    private void produceAfterCommit(Object entityId, ProducedReplication<?> replication) {
        ProducerRecord<Object, ProducedReplication<?>> record = new ProducerRecord<>(topic, entityId, replication);
        ReplicationTransactionCallback transactionCallback = new ReplicationTransactionCallback(kafkaTemplate, record);
        transactionManager.register(transactionCallback);
    }
}
