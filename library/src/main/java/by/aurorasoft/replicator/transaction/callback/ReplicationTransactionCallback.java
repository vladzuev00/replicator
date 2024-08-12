package by.aurorasoft.replicator.transaction.callback;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.support.TransactionSynchronization;

@RequiredArgsConstructor
@Getter
public final class ReplicationTransactionCallback implements TransactionSynchronization {
    private final KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate;
    private final ProducerRecord<Object, ProducedReplication<?>> record;

    @Override
    public void afterCommit() {
        kafkaTemplate.send(record);
    }
}
