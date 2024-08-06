package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import org.springframework.kafka.core.KafkaTemplate;

public final class SaveReplicationProducer extends ReplicationProducer<SaveProducedReplication, Object> {

    public SaveReplicationProducer(String topicName, KafkaTemplate<Object, SaveProducedReplication> kafkaTemplate) {
        super(topicName, kafkaTemplate);
    }
}
