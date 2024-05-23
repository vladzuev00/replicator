package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.exception.RelatedReplicationNotDeliveredException;
import by.aurorasoft.replicator.holder.producer.ReplicationProducerHolder;
import by.aurorasoft.replicator.holder.producer.ReplicationProducerHolderFactory;
import by.aurorasoft.replicator.holder.service.ReplicatedServiceHolder;
import by.aurorasoft.replicator.holder.service.ReplicatedServiceHolderFactory;
import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

@Configuration
@ComponentScan("by.aurorasoft.replicator")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ReplicationConfig {

    @Bean
    public ReplicatedServiceHolder replicatedServiceHolder(final ReplicatedServiceHolderFactory factory) {
        return factory.create();
    }

    @Bean
    public ReplicationProducerHolder replicationProducerHolder(final ReplicationProducerHolderFactory factory) {
        return factory.create();
    }

    @Bean
    public RetryTemplate replicationRetryTemplate(final ReplicationRetryConsumeProperty property) {
        return new RetryTemplateBuilder()
                .fixedBackoff(property.getTimeLapseMs())
                .maxAttempts(property.getMaxAttempts())
                .retryOn(RelatedReplicationNotDeliveredException.class)
                .build();
    }
}
