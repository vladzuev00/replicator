package by.aurorasoft.replicator.producer;

import by.aurorasoft.kafka.producer.KafkaProducerAbstractSimple;
import by.aurorasoft.replicator.model.produced.ProducedReplication;
import org.springframework.kafka.core.KafkaTemplate;

public final class ReplicationProducer<ID> extends KafkaProducerAbstractSimple<ID, ProducedReplication<ID>> {

    public ReplicationProducer(final String topicName, final KafkaTemplate<ID, ProducedReplication<ID>> kafkaTemplate) {
        super(topicName, kafkaTemplate);
    }

    @Override
    public void send(final ProducedReplication<ID> replication) {
        sendModel(replication.getEntityId(), replication);
    }
}
