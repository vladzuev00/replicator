package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import org.springframework.kafka.core.KafkaTemplate;

public final class DeleteReplicationProducer extends ReplicationProducer<DeleteProducedReplication, Object> {

    public DeleteReplicationProducer(String topicName, KafkaTemplate<Object, DeleteProducedReplication> kafkaTemplate) {
        super(topicName, kafkaTemplate);
    }
}
