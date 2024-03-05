package by.aurorasoft.replicator.consumer;

import by.aurorasoft.kafka.consumer.KafkaConsumerAbstract;
import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;

@RequiredArgsConstructor
public final class KafkaReplicationConsumer<ID, DTO extends AbstractDto<ID>> extends KafkaConsumerAbstract<ID, Replication<ID, DTO>> {
    private final AbsServiceCRUD<ID, ?, DTO, ?> service;

    @Getter
    private final String groupId;

    @Getter
    private final String topic;

    @Getter
    private final Deserializer<ID> idDeserializer;

    @Override
    public void listen(final ConsumerRecord<ID, Replication<ID, DTO>> record) {
        record.value().execute(service);
    }
}
