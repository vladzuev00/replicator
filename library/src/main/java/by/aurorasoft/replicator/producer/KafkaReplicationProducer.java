package by.aurorasoft.replicator.producer;

import by.aurorasoft.kafka.producer.KafkaProducerAbstractSimple;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import org.springframework.kafka.core.KafkaTemplate;

public final class KafkaReplicationProducer extends KafkaProducerAbstractSimple<Object, ProducedReplication<?>> {

    public KafkaReplicationProducer(String topicName, KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate) {
        super(topicName, kafkaTemplate);
    }

    @Override
    public void send(ProducedReplication<?> replication) {
        sendModel(replication.getEntityId(), replication);
    }
}
