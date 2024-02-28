package by.aurorasoft.replicator.producer;

import by.aurorasoft.kafka.producer.KafkaProducerAbstract;
import by.aurorasoft.replicator.model.TransportableReplication;
import by.aurorasoft.replicator.model.replication.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;

//TODO: refactor
public final class KafkaReplicationProducer<ID, DTO extends AbstractDto<ID>> extends KafkaProducerAbstract<ID, String, TransportableReplication, Replication<ID, DTO>> {
    private final ObjectMapper objectMapper;

    public KafkaReplicationProducer(final String topicName,
                                    final KafkaTemplate<ID, String> kafkaTemplate,
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
        final String dtoJson = serialize(replication.getDto());
        return new TransportableReplication(replication.getType(), dtoJson);
    }

    @Override
    protected String convertTransportableToTopicValue(final TransportableReplication replication) {
        return serialize(replication);
    }

    private String serialize(final Object object) {
        try {
            return objectMapper.writeValueAsString(object);
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
