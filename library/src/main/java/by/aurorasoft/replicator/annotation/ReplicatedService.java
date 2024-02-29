package by.aurorasoft.replicator.annotation;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Service;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Service
@Target(TYPE)
@Retention(RUNTIME)
public @interface ReplicatedService {
    ProducerConfig producerConfig();
    TopicConfig topicConfig();

    @interface ProducerConfig {
        Class<? extends Serializer<?>> idSerializer();

        int producerBatchSize() default 10;

        int producerLingerMs() default 500;

        int producerDeliveryTimeoutMs() default 100000;
    }

    @interface TopicConfig {
        String topicName();

        int topicPartitionCount() default 1;

        int topicReplicationFactor() default 1;
    }
}
