package by.aurorasoft.replicator.producer;

import by.aurorasoft.kafka.producer.KafkaProducerGenericRecordIntermediaryHooks;
import by.aurorasoft.replicator.model.TransportableReplication;
import by.aurorasoft.replicator.model.replication.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.core.KafkaTemplate;

public final class KafkaReplicationProducer<ID, DTO extends AbstractDto<ID>>
        extends KafkaProducerGenericRecordIntermediaryHooks<ID, TransportableReplication, Replication<ID, DTO>> {
    private final ObjectMapper objectMapper;

    public KafkaReplicationProducer(final String topicName,
                                    final KafkaTemplate<ID, GenericRecord> kafkaTemplate,
                                    final Schema schema,
                                    final ObjectMapper objectMapper) {
        super(topicName, kafkaTemplate, schema);
        this.objectMapper = objectMapper;
    }

    @Override
    protected ID getTopicKey(final Replication<ID, DTO> replication) {
        return replication.getEntityId();
    }

    @Override
    protected TransportableReplication convertModelToTransportable(final Replication<ID, DTO> replication) {
        try {
            final String dtoJson = objectMapper.writeValueAsString(replication.getDto());
            return new TransportableReplication(replication.getType(), dtoJson);
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
