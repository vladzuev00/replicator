package by.aurorasoft.replicator.transaction.callback;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.support.TransactionSynchronization;

@RequiredArgsConstructor
public final class ProduceReplicationTransactionCallback implements TransactionSynchronization {
    private final KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate;
    private final ProducedReplication<?> replication;
    private final String topic;

    @Override
    public void afterCommit() {
        throw new UnsupportedOperationException();
//        ProducerRecord<Object, ProducedReplication<?>> record = new ProducerRecord<>(topic, entityId, replication);
//        kafkaTemplate.send(record);
    }
}
