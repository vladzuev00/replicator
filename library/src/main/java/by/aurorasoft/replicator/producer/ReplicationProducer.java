package by.aurorasoft.replicator.producer;

import by.aurorasoft.kafka.producer.KafkaProducerAbstract;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import org.springframework.kafka.core.KafkaTemplate;

public abstract class ReplicationProducer<BODY> extends KafkaProducerAbstract<Object, ProducedReplication<?>, BODY, Object> {


    public ReplicationProducer(String topicName, KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate) {
        super(topicName, kafkaTemplate);
    }

    @Override
    public final void send(Object model) {
        sendModel(model);
    }

    @Override
    protected final BODY convertModelToTransportable(Object model) {
        return createReplicationBody(model);
    }

    @Override
    protected final ProducedReplication<?> convertTransportableToTopicValue(BODY body) {
        return createReplication(body);
    }

    protected abstract BODY createReplicationBody(Object model);

    protected abstract ProducedReplication<?> createReplication(BODY body);
}
