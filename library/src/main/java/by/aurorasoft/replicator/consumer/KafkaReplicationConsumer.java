package by.aurorasoft.replicator.consumer;

import by.aurorasoft.kafka.consumer.KafkaConsumerAbstract;
import by.aurorasoft.replicator.model.TransportableReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import java.lang.reflect.Method;
import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@RequiredArgsConstructor
public final class KafkaReplicationConsumer<ID, DTO extends AbstractDto<ID>> extends KafkaConsumerAbstract<ID, TransportableReplication> {
    private static final String METHOD_NAME_PROCESSING_RECORDS = "listen";

    private final AbsServiceCRUD<ID, ?, DTO, ?> service;
    private final ObjectMapper objectMapper;
    private final Class<DTO> dtoType;
    private final String bootstrapAddress;
    private final String groupId;
    private final String topic;
    private final Deserializer<ID> idDeserializer;

    @Override
    public void listen(final ConsumerRecord<ID, TransportableReplication> record) {
        final TransportableReplication replication = record.value();
        final DTO dto = deserializeDto(replication.getDtoJson());
        replication.getType().createReplication(dto).execute(service);
    }

    public void start() {
        createListenerContainerFactory()
                .createListenerContainer(createListenerEndpoint())
                .start();
    }

    private DTO deserializeDto(final String dtoJson) {
        try {
            return objectMapper.readValue(dtoJson, dtoType);
        } catch (final JsonProcessingException cause) {
            throw new ReplicationConsumingException(cause);
        }
    }

    private ConcurrentKafkaListenerContainerFactory<ID, TransportableReplication> createListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<ID, TransportableReplication> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory());
        return factory;
    }

    private ConsumerFactory<ID, TransportableReplication> createConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(createConfigsByNames(), idDeserializer, createReplicationDeserializer());
    }

    private Map<String, Object> createConfigsByNames() {
        return Map.of(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress, GROUP_ID_CONFIG, groupId);
    }

    private JsonDeserializer<TransportableReplication> createReplicationDeserializer() {
        return new JsonDeserializer<>(TransportableReplication.class);
    }

    private KafkaListenerEndpoint createListenerEndpoint() {
        final MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
        endpoint.setGroupId(groupId);
        endpoint.setAutoStartup(true);
        endpoint.setTopics(topic);
        endpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
        endpoint.setBean(this);
        endpoint.setMethod(getMethodProcessingRecords());
        return endpoint;
    }

    private static Method getMethodProcessingRecords() {
        try {
            return KafkaReplicationConsumer.class.getMethod(METHOD_NAME_PROCESSING_RECORDS, ConsumerRecord.class);
        } catch (final NoSuchMethodException cause) {
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
