package by.aurorasoft.replicator.annotation;

import org.apache.kafka.common.serialization.Serializer;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//TODO try remove @Inherited
@Inherited
@Target(TYPE)
@Retention(RUNTIME)
public @interface ReplicatedRepository {
    Producer producer();

    Topic topic();

    EntityView[] entityViews() default {};

    @interface Producer {
        Class<? extends Serializer<?>> idSerializer();

        int batchSize() default 10;

        int lingerMs() default 500;

        int deliveryTimeoutMs() default 100000;
    }

    @interface Topic {
        String name();

        int partitionCount() default 1;

        short replicationFactor() default 1;
    }

    @interface EntityView {
        Class<?> type();

        String[] excludedFields() default {};
    }
}
