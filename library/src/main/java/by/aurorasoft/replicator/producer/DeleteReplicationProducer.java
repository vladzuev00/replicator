package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import org.springframework.kafka.core.KafkaTemplate;

public final class DeleteReplicationProducer extends ReplicationProducer<Object> {

    public DeleteReplicationProducer(String topicName, KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate) {
        super(topicName, kafkaTemplate);
    }

    @Override
    protected Object getEntityId(Object entityId) {
        return entityId;
    }

    @Override
    protected Object createReplicationBody(Object entityId) {
        return entityId;
    }

    @Override
    protected ProducedReplication<?> createReplication(Object entityId) {
        return new DeleteProducedReplication(entityId);
    }
}
