package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import org.springframework.kafka.core.KafkaTemplate;

public final class SaveReplicationProducer extends ReplicationProducer<EntityJsonView<?>> {

    public SaveReplicationProducer(String topicName, KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate) {
        super(topicName, kafkaTemplate);
    }

    @Override
    protected EntityJsonView<?> createReplicationBody(Object entity) {
        return null;
    }

    @Override
    protected ProducedReplication<?> createReplication(EntityJsonView<?> entityJsonView) {
        return null;
    }
}
