package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.transaction.callback.ProduceReplicationTransactionCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

@RequiredArgsConstructor
public final class ReplicationProducer {
    private final SaveProducedReplicationFactory saveReplicationFactory;
    private final KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate;
    private final String topic;
    private final ReplicatedService.ViewConfig[] entityViewSettings;

    public void produceSaveAfterCommit(Object savedEntity) {
//        SaveProducedReplication replication = saveReplicationFactory.create(savedEntity, entityViewSettings);
//        produceAfterCommit(replication);
    }

    public void produceDeleteAfterCommit(Object entityId) {
        DeleteProducedReplication replication = new DeleteProducedReplication(entityId);
        produceAfterCommit(replication);
    }

    private void produceAfterCommit(ProducedReplication<?> replication) {
        var transactionCallback = new ProduceReplicationTransactionCallback(kafkaTemplate, replication, topic);
        registerSynchronization(transactionCallback);
    }
}
