package by.aurorasoft.replicator;

import by.aurorasoft.replicator.consumer.KafkaReplicationConsumer;
import by.aurorasoft.replicator.consumer.KafkaReplicationConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public final class ReplicationListenerEndpointFactory {
    private static final String METHOD_NAME_PROCESSING_REPLICATION = "listen";

    public KafkaListenerEndpoint create(final KafkaReplicationConsumer<?, ?> consumer) {
        final KafkaReplicationConsumerConfig<?, ?> consumerConfig = consumer.getConfig();
        final MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
        endpoint.setGroupId(consumerConfig.getGroupId());
        endpoint.setAutoStartup(true);
        endpoint.setTopics(consumerConfig.getTopic());
        endpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
        endpoint.setBean(consumer);
        endpoint.setMethod(getMethodProcessingReplication());
        return endpoint;
    }

    private static Method getMethodProcessingReplication() {
        try {
            return KafkaReplicationConsumer.class.getMethod(METHOD_NAME_PROCESSING_REPLICATION, ConsumerRecord.class);
        } catch (final NoSuchMethodException cause) {
            throw new NoMethodProcessingReplicationException(
                    "Method processing replication should be named as %s".formatted(METHOD_NAME_PROCESSING_REPLICATION)
            );
        }
    }

    static final class NoMethodProcessingReplicationException extends RuntimeException {

        @SuppressWarnings("unused")
        public NoMethodProcessingReplicationException() {

        }

        public NoMethodProcessingReplicationException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoMethodProcessingReplicationException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoMethodProcessingReplicationException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
