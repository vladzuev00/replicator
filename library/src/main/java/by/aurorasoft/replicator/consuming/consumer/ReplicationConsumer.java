package by.aurorasoft.replicator.consuming.consumer;

import by.aurorasoft.kafka.consumer.KafkaConsumerAbstract;
import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@RequiredArgsConstructor
@Getter
public final class ReplicationConsumer<ID, DTO extends AbstractDto<ID>> extends KafkaConsumerAbstract<ID, Replication<ID, DTO>> {
    private final ReplicationConsumerConfig<ID, DTO> config;

    @Override
    public void listen(final ConsumerRecord<ID, Replication<ID, DTO>> record) {
        record.value().execute(config.getService());
    }
}