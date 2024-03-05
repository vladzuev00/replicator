package by.aurorasoft.replicator.consumer;

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

    public void start(final KafkaReplicationConsumer<?, ?> consumer) {
        createListenerContainerFactory(consumer)
                .createListenerContainer(createListenerEndpoint(consumer))
                .start();
    }

    private <ID> ConcurrentKafkaListenerContainerFactory<ID, TransportableReplication> createListenerContainerFactory(
            final KafkaReplicationConsumer<ID, ?> consumer
    ) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<ID, TransportableReplication>();
        factory.setConsumerFactory(createConsumerFactory(consumer));
        return factory;
    }

    private <ID> ConsumerFactory<ID, TransportableReplication> createConsumerFactory(
            final KafkaReplicationConsumer<ID, ?> consumer
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

    private JsonDeserializer<TransportableReplication> createReplicationDeserializer() {
        return new JsonDeserializer<>(TransportableReplication.class);
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
