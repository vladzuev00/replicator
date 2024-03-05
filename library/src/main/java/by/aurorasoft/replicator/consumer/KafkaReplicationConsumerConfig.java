package by.aurorasoft.replicator.consumer;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
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
}
