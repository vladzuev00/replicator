package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.exception.RelationReplicationNotDeliveredException;
import by.aurorasoft.replicator.factory.ReplicationProducerHolderFactory;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
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

    @Bean
    public ReplicationProducerHolder replicationProducerHolder(final ReplicationProducerHolderFactory factory) {
        return factory.create();
    }

    @Bean("replicationRetryTemplate")
    public RetryTemplate retryTemplate(final ReplicationRetryConsumeProperty property) {
        return new RetryTemplateBuilder()
                .fixedBackoff(property.getTimeLapseMs())
                .maxAttempts(property.getMaxAttempts())
                .retryOn(RelationReplicationNotDeliveredException.class)
                .build();
    }
}
