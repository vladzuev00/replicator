package by.aurorasoft.replicator.consumer;

import by.aurorasoft.kafka.consumer.KafkaConsumerGenericRecordBatch;
import by.aurorasoft.replicator.model.ReplicationType;
import by.aurorasoft.replicator.model.replication.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

import static by.aurorasoft.replicator.model.ReplicationType.valueOf;
import static by.aurorasoft.replicator.model.TransportableReplication.Fields.dtoJson;
import static by.aurorasoft.replicator.model.TransportableReplication.Fields.type;

@RequiredArgsConstructor
public abstract class KafkaReplicationConsumer<ID, DTO extends AbstractDto<ID>>
        extends KafkaConsumerGenericRecordBatch<ID, Replication<ID, DTO>> {
    private final AbsServiceCRUD<ID, ?, DTO, ?> service;
    private final ObjectMapper objectMapper;
    private final Class<DTO> dtoType;

    @Override
    public void listen(final List<ConsumerRecord<ID, GenericRecord>> records) {
        records.stream()
                .map(this::map)
                .forEach(this::execute);
    }

    @Override
    protected final Replication<ID, DTO> map(final GenericRecord record) {
        final DTO dto = getDto(record);
        return getReplicationType(record).createReplication(dto);
    }

    private void execute(final Replication<ID, DTO> replication) {
        replication.execute(service);
    }

    private ReplicationType getReplicationType(final GenericRecord record) {
        return valueOf(getString(record, type));
    }

    private DTO getDto(final GenericRecord record) {
        try {
            return objectMapper.readValue(getDtoJson(record), dtoType);
        } catch (final JsonProcessingException cause) {
            throw new ReplicationConsumingException(cause);
        }
    }

    private String getDtoJson(final GenericRecord record) {
        return getString(record, dtoJson);
    }

    static final class ReplicationConsumingException extends RuntimeException {

        @SuppressWarnings("unused")
        public ReplicationConsumingException() {

        }

        @SuppressWarnings("unused")
        public ReplicationConsumingException(final String description) {
            super(description);
        }

        public ReplicationConsumingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ReplicationConsumingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
