package by.aurorasoft.replicator.transaction.callback;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.support.TransactionSynchronization;

@RequiredArgsConstructor
public final class ProduceReplicationTransactionCallback implements TransactionSynchronization {
    private final KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate;
    private final ProducedReplication<?> replication;
    private final String topic;

    @Override
    public void afterCommit() {
        var record = new ProducerRecord<Object, ProducedReplication<?>>(topic, replication.getEntityId(), replication);
        kafkaTemplate.send(record);
    }
}
