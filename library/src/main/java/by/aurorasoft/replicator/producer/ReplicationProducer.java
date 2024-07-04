package by.aurorasoft.replicator.producer;

import by.aurorasoft.kafka.producer.KafkaProducerAbstractSimple;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import org.springframework.kafka.core.KafkaTemplate;

public final class ReplicationProducer extends KafkaProducerAbstractSimple<Object, ProducedReplication> {

    public ReplicationProducer(String topicName, KafkaTemplate<Object, ProducedReplication> kafkaTemplate) {
        super(topicName, kafkaTemplate);
    }

    @Override
    public void send(ProducedReplication replication) {
        sendModel(replication.getEntityId(), replication);
    }
}
