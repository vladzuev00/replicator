package by.aurorasoft.replicator.annotation;

import by.aurorasoft.replicator.config.ReplicationConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("unused")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ReplicationConfig.class)
public @interface EnableReplication {

}
