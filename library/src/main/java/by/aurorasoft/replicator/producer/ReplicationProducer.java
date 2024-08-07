package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public abstract class ReplicationProducer<BODY> {
    private final KafkaTemplate<Object, ProducedReplication<BODY>> kafkaTemplate;
    private final String topicName;

    public final void send(Object model) {
        Object entityId = getEntityId(model);
        BODY body = createBody(model);
        ProducedReplication<BODY> replication = createReplication(body);
        var record = new ProducerRecord<>(topicName, entityId, replication);
        kafkaTemplate.send(record);
    }

    protected abstract Object getEntityId(Object model);

    protected abstract BODY createBody(Object model);

    protected abstract ProducedReplication<BODY> createReplication(BODY body);
}
