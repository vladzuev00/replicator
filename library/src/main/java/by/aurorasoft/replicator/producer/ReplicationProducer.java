package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public final class ReplicationProducer {
    private final KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate;
    private final String topicName;

    public void send(ProducedReplication<?> replication) {
        Object entityId = replication.getEntityId();
        ProducerRecord<Object, ProducedReplication<?>> record = new ProducerRecord<>(topicName, entityId, replication);
        kafkaTemplate.send(record);
    }
}
