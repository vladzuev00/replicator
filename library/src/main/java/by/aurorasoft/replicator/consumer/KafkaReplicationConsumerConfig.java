package by.aurorasoft.replicator.consumer;

import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;

@RequiredArgsConstructor
@Getter
public final class KafkaReplicationConsumerConfig<ID, DTO extends AbstractDto<ID>> {
    private final String groupId;
    private final String topic;
    private final Deserializer<ID> idDeserializer;
    private final AbsServiceCRUD<ID, ?, DTO, ?> service;
    private final TypeReference<Replication<ID, DTO>> replicationTypeReference;
}
