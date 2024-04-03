package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.exception.PerhapsRelationNotDeliveredYetException;
import by.aurorasoft.replicator.factory.ReplicationProducerHolderFactory;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("by.aurorasoft.replicator")
public class ReplicationConfig {

    //TODO: do class with this two fields
    @Value("${replication.consume.retry.time-lapse-ms}")
    private long timeLapseMs;

    @Value("${replication.consume.retry.max-attempts}")
    private int maxAttempts;

    @Bean
    public ReplicationProducerHolder replicationProducerHolder(final ReplicationProducerHolderFactory factory) {
        return factory.create();
    }

    //TODO: do class with this two fields
    @Bean("replicationRetryTemplate")
    public RetryTemplate retryTemplate() {
        return new RetryTemplateBuilder()
                .fixedBackoff(timeLapseMs)
                .maxAttempts(maxAttempts)
                .retryOn(PerhapsRelationNotDeliveredYetException.class)
                .build();
    }
}
