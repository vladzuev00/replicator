package by.aurorasoft.replicator.consumer;

import by.aurorasoft.kafka.consumer.KafkaConsumerAbstract;
import by.aurorasoft.replicator.model.TransportableReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@RequiredArgsConstructor
public abstract class KafkaReplicationConsumer<ID, DTO extends AbstractDto<ID>> extends KafkaConsumerAbstract<ID, String> {
    private final AbsServiceCRUD<ID, ?, DTO, ?> service;
    private final ObjectMapper objectMapper;
    private final Class<DTO> dtoType;

    @Override
    public void listen(final ConsumerRecord<ID, String> consumerRecord) {
        final TransportableReplication replication = getReplication(consumerRecord);
        final DTO dto = getDto(replication);
        replication.getType().createReplication(dto).execute(service);
    }

    private TransportableReplication getReplication(final ConsumerRecord<ID, String> consumerRecord) {
        return deserialize(consumerRecord.value(), TransportableReplication.class);
    }

    private DTO getDto(final TransportableReplication replication) {
        return deserialize(replication.getDtoJson(), dtoType);
    }

    private <T> T deserialize(final String json, final Class<T> objectType) {
        try {
            return objectMapper.readValue(json, objectType);
        } catch (final JsonProcessingException cause) {
            throw new ReplicationConsumingException(cause);
        }
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
