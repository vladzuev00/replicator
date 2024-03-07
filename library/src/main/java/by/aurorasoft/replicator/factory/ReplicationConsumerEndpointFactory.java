package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public final class ReplicationConsumerEndpointFactory {
    static final String PROCESSING_METHOD_NAME = "listen";

    public KafkaListenerEndpoint create(final ReplicationConsumer<?, ?> consumer) {
        final MethodKafkaListenerEndpoint<?, ?> endpoint = new MethodKafkaListenerEndpoint<>();
        endpoint.setGroupId(consumer.getGroupId());
        endpoint.setAutoStartup(true);
        endpoint.setTopics(consumer.getTopic());
        endpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
        endpoint.setBean(consumer);
        endpoint.setMethod(getProcessingMethod());
        return endpoint;
    }

    private static Method getProcessingMethod() {
        try {
            return ReplicationConsumer.class.getMethod(PROCESSING_METHOD_NAME, ConsumerRecord.class);
        } catch (final NoSuchMethodException cause) {
            throw new NoProcessingMethodException("Method '%s' doesn't exist".formatted(PROCESSING_METHOD_NAME));
        }
    }

    private static final class NoProcessingMethodException extends RuntimeException {

        @SuppressWarnings("unused")
        public NoProcessingMethodException() {

        }

        public NoProcessingMethodException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoProcessingMethodException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoProcessingMethodException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
