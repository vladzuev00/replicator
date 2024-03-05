package by.aurorasoft.replicator.producer;

import by.aurorasoft.kafka.producer.KafkaProducerAbstract;
import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;

public final class KafkaReplicationProducer<ID, DTO extends AbstractDto<ID>> extends KafkaProducerAbstract<ID, TransportableReplication, TransportableReplication, Replication<ID, DTO>> {
    private final ObjectMapper objectMapper;

    public KafkaReplicationProducer(final String topicName,
                                    final KafkaTemplate<ID, TransportableReplication> kafkaTemplate,
                                    final ObjectMapper objectMapper) {
        super(topicName, kafkaTemplate);
        this.objectMapper = objectMapper;
    }

    @Override
    public void send(final Replication<ID, DTO> replication) {
        sendModel(replication.getEntityId(), replication);
    }

    @Override
    protected TransportableReplication convertModelToTransportable(final Replication<ID, DTO> replication) {
//        final String dtoJson = serialize(replication.getDto());
//        return new TransportableReplication(replication.getType(), dtoJson);
        return null;
    }

    @Override
    protected TransportableReplication convertTransportableToTopicValue(final TransportableReplication replication) {
        return replication;
    }

    private String serialize(final DTO dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (final JsonProcessingException cause) {
            throw new ReplicationProducingException(cause);
        }
    }

    private static final class ReplicationProducingException extends RuntimeException {

        @SuppressWarnings("unused")
        public ReplicationProducingException() {

        }

        @SuppressWarnings("unused")
        public ReplicationProducingException(final String description) {
            super(description);
        }

        public ReplicationProducingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ReplicationProducingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
