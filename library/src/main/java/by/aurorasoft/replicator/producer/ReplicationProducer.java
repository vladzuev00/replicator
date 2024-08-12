package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting.EntityViewSetting;
import by.aurorasoft.replicator.transaction.callback.ProduceReplicationTransactionCallback;
import by.aurorasoft.replicator.transaction.manager.ReplicationTransactionManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

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
        produceAfterCommit(replication);
    }

    public void produceDeleteAfterCommit(Object entityId) {
        DeleteProducedReplication replication = new DeleteProducedReplication(entityId);
        produceAfterCommit(replication);
    }

    private void produceAfterCommit(ProducedReplication<?> replication) {
        var transactionCallback = new ProduceReplicationTransactionCallback(kafkaTemplate, replication, topic);
        transactionManager.executeAfterCommit(transactionCallback);
    }
}
