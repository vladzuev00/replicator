package by.aurorasoft.replicator.consumer;

import by.aurorasoft.replicator.model.TransportableReplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@Component
public final class KafkaReplicationConsumersStarter {
    private static final String METHOD_NAME_PROCESSING_RECORDS = "listen";

    private final String bootstrapAddress;
    private final ObjectMapper objectMapper;
    private final List<KafkakaReplicationConsumerStarter<?, ?>> consumerConfigs;

    public KafkaReplicationConsumersStarter(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress,
                                            final ObjectMapper objectMapper,
                                            final List<KafkakaReplicationConsumerStarter<?, ?>> consumerConfigs) {
        this.bootstrapAddress = bootstrapAddress;
        this.objectMapper = objectMapper;
        this.consumerConfigs = consumerConfigs;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        consumerConfigs.forEach(this::startConsumer);
    }

    private void startConsumer(final KafkakaReplicationConsumerStarter<?, ?> config) {
        createListenerContainerFactory(config)
                .createListenerContainer(createListenerEndpoint(config))
                .start();
    }

    private <ID> ConcurrentKafkaListenerContainerFactory<ID, TransportableReplication> createListenerContainerFactory(final KafkakaReplicationConsumerStarter<ID, ?> config) {
        final ConcurrentKafkaListenerContainerFactory<ID, TransportableReplication> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(config));
        return factory;
    }

    private <ID> ConsumerFactory<ID, TransportableReplication> createConsumerFactory(final KafkakaReplicationConsumerStarter<ID, ?> config) {
        return new DefaultKafkaConsumerFactory<>(
                createConfigsByNames(config),
                config.getIdDeserializer(),
                createReplicationDeserializer()
        );
    }

    private Map<String, Object> createConfigsByNames(final KafkakaReplicationConsumerStarter<?, ?> config) {
        return Map.of(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress, GROUP_ID_CONFIG, config.getConsumerGroupId());
    }

    private JsonDeserializer<TransportableReplication> createReplicationDeserializer() {
        return new JsonDeserializer<>(TransportableReplication.class);
    }

    private KafkaListenerEndpoint createListenerEndpoint(final KafkakaReplicationConsumerStarter<?, ?> consumerConfig) {
        final MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
        endpoint.setGroupId(consumerConfig.getConsumerGroupId());
        endpoint.setAutoStartup(true);
        endpoint.setTopics(consumerConfig.getTopic());
        endpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
        endpoint.setBean(createConsumer(consumerConfig));
        endpoint.setMethod(getMethodProcessingRecords());
        return endpoint;
    }

    private KafkaReplicationConsumer createConsumer(final KafkakaReplicationConsumerStarter<?, ?> consumerConfig) {
        return new KafkaReplicationConsumer(consumerConfig.getService(), objectMapper, consumerConfig.getDtoType());
    }

    private static Method getMethodProcessingRecords() {
        try {
            return KafkaReplicationConsumer.class.getMethod(METHOD_NAME_PROCESSING_RECORDS, ConsumerRecord.class);
        } catch (final NoSuchMethodException cause) {
            throw new StartReplicationConsumingException(cause);
        }
    }

    static final class StartReplicationConsumingException extends RuntimeException {

        @SuppressWarnings("unused")
        public StartReplicationConsumingException() {

        }

        @SuppressWarnings("unused")
        public StartReplicationConsumingException(final String description) {
            super(description);
        }

        public StartReplicationConsumingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public StartReplicationConsumingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
