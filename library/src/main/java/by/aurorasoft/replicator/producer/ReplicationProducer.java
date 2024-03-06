package by.aurorasoft.replicator.producer;

import by.aurorasoft.kafka.producer.KafkaProducerAbstractSimple;
import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import org.springframework.kafka.core.KafkaTemplate;

public final class ReplicationProducer<ID, DTO extends AbstractDto<ID>> extends KafkaProducerAbstractSimple<ID, Replication<ID, DTO>> {

    public ReplicationProducer(final String topicName, final KafkaTemplate<ID, Replication<ID, DTO>> kafkaTemplate) {
        super(topicName, kafkaTemplate);
    }

    @Override
    public void send(final Replication<ID, DTO> replication) {
        sendModel(replication.getEntityId(), replication);
    }
}
