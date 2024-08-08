//package by.aurorasoft.replicator.producer;
//
//import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
//import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
//import org.springframework.kafka.core.KafkaTemplate;
//
//public final class DeleteReplicationProducer extends ReplicationProducer<Object> {
//
//    public DeleteReplicationProducer(KafkaTemplate<Object, ProducedReplication<Object>> kafkaTemplate, String topicName) {
//        super(kafkaTemplate, topicName);
//    }
//
//    @Override
//    protected Object getEntityId(Object entityId) {
//        return entityId;
//    }
//
//    @Override
//    protected Object createBody(Object entityId) {
//        return entityId;
//    }
//
//    @Override
//    protected ProducedReplication<Object> createReplication(Object entityId) {
//        return new DeleteProducedReplication(entityId);
//    }
//}
