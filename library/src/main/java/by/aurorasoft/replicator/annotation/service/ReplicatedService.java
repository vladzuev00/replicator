package by.aurorasoft.replicator.annotation.service;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Service
@Target(TYPE)
@Transactional
@Retention(RUNTIME)
public @interface ReplicatedService {
    ProducerConfig producerConfig();

    TopicConfig topicConfig();

    DtoViewConfig[] dtoViewConfigs() default {};

    @interface ProducerConfig {
        Class<? extends Serializer<?>> idSerializer();

        int batchSize() default 10;

        int lingerMs() default 500;

        int deliveryTimeoutMs() default 100000;
    }

    @interface TopicConfig {
        String name();

        int partitionCount() default 1;

        short replicationFactor() default 1;
    }

    @interface DtoViewConfig {
        Class<?> type();

        String[] includedFields() default {};

        String[] excludedFields() default {};
    }
}
