package by.aurorasoft.replicator.consumer;

import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@Component
public final class KafkaReplicationConsumerStarter {
    private static final String METHOD_NAME_PROCESSING_RECORDS = "listen";

    private final String bootstrapAddress;

    public KafkaReplicationConsumerStarter(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress) {
        this.bootstrapAddress = bootstrapAddress;
    }

    public <ID, DTO extends AbstractDto<ID>> void start(final KafkaReplicationConsumer<ID, DTO> consumer) {
        createListenerContainerFactory(consumer)
                .createListenerContainer(createListenerEndpoint(consumer))
                .start();
    }

    private <ID, DTO extends AbstractDto<ID>> ConcurrentKafkaListenerContainerFactory<ID, Replication<ID, DTO>> createListenerContainerFactory(
            final KafkaReplicationConsumer<ID, DTO> consumer
    ) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<ID, Replication<ID, DTO>>();
        factory.setConsumerFactory(createConsumerFactory(consumer));
        return factory;
    }

    private <ID, DTO extends AbstractDto<ID>> ConsumerFactory<ID, Replication<ID, DTO>> createConsumerFactory(
            final KafkaReplicationConsumer<ID, DTO> consumer
    ) {
        return new DefaultKafkaConsumerFactory<>(
                createConfigsByNames(consumer),
                consumer.getIdDeserializer(),
                createReplicationDeserializer()
        );
    }

    private Map<String, Object> createConfigsByNames(final KafkaReplicationConsumer<?, ?> consumer) {
        return Map.of(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress, GROUP_ID_CONFIG, consumer.getGroupId());
    }

    private <ID, DTO extends AbstractDto<ID>> JsonDeserializer<Replication<ID, DTO>> createReplicationDeserializer() {
        return new JsonDeserializer<>(Replication.class);
    }

    private KafkaListenerEndpoint createListenerEndpoint(final KafkaReplicationConsumer<?, ?> consumer) {
        final MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
        endpoint.setGroupId(consumer.getGroupId());
        endpoint.setAutoStartup(true);
        endpoint.setTopics(consumer.getTopic());
        endpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
        endpoint.setBean(consumer);
        endpoint.setMethod(getMethodProcessingRecords());
        return endpoint;
    }

    private static Method getMethodProcessingRecords() {
        try {
            return KafkaReplicationConsumer.class.getMethod(METHOD_NAME_PROCESSING_RECORDS, ConsumerRecord.class);
        } catch (final NoSuchMethodException cause) {
            throw new ReplicationConsumerStartingException(cause);
        }
    }

    static final class ReplicationConsumerStartingException extends RuntimeException {

        @SuppressWarnings("unused")
        public ReplicationConsumerStartingException() {

        }

        @SuppressWarnings("unused")
        public ReplicationConsumerStartingException(final String description) {
            super(description);
        }

        public ReplicationConsumerStartingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public ReplicationConsumerStartingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
