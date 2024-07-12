package by.aurorasoft.replicator.annotation;

import by.aurorasoft.replicator.config.ReplicationConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@SuppressWarnings("unused")
@Import(ReplicationConfig.class)
public @interface EnableReplication {

}
