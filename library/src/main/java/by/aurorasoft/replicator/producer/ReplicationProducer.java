package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.transactioncallback.ProduceReplicationTransactionCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

@RequiredArgsConstructor
public final class ReplicationProducer {
    private final SaveProducedReplicationFactory saveReplicationFactory;
    private final KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate;
    private final String topic;
    private final DtoViewConfig[] dtoViewConfigs;

    public void produceSaveAfterCommit(Object savedDto) {
        SaveProducedReplication replication = saveReplicationFactory.create(savedDto, dtoViewConfigs);
        produceAfterCommit(replication);
    }

    public void produceDeleteAfterCommit(Object dtoId) {
        DeleteProducedReplication replication = new DeleteProducedReplication(dtoId);
        produceAfterCommit(replication);
    }

    private void produceAfterCommit(ProducedReplication<?> replication) {
        var transactionCallback = new ProduceReplicationTransactionCallback(topic, replication, kafkaTemplate);
        registerSynchronization(transactionCallback);
    }
}
