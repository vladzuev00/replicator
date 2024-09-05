package by.aurorasoft.replicator.transactioncallback;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.support.TransactionSynchronization;

@RequiredArgsConstructor
public final class ProduceReplicationTransactionCallback implements TransactionSynchronization {
    private final String topic;
    private final ProducedReplication<?> replication;
    private final KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate;

    @Override
    public void afterCommit() {
        var record = new ProducerRecord<Object, ProducedReplication<?>>(topic, replication.getDtoId(), replication);
        kafkaTemplate.send(record);
    }
}
