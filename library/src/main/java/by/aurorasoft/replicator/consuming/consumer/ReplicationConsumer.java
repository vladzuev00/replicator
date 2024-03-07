package by.aurorasoft.replicator.consuming.consumer;

import by.aurorasoft.kafka.consumer.KafkaConsumerAbstract;
import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;

import static lombok.AccessLevel.NONE;

@RequiredArgsConstructor
@Getter
public final class ReplicationConsumer<ID, DTO extends AbstractDto<ID>>
        extends KafkaConsumerAbstract<ID, Replication<ID, DTO>> {
    private final String groupId;
    private final String topic;
    private final Deserializer<ID> idDeserializer;
    private final TypeReference<Replication<ID, DTO>> replicationTypeReference;

    @Getter(NONE)
    private final AbsServiceCRUD<ID, ?, DTO, ?> service;

    @Override
    public void listen(final ConsumerRecord<ID, Replication<ID, DTO>> record) {
        record.value().execute(service);
    }
}
