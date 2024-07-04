package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.factory.ReplicationRetryTemplateFactory;
import by.aurorasoft.replicator.holder.producer.ReplicationProducerRegistry;
import by.aurorasoft.replicator.holder.producer.ReplicationProducerRegistryFactory;
import by.aurorasoft.replicator.holder.service.ReplicatedServiceRegistry;
import by.aurorasoft.replicator.holder.service.ReplicatedServiceRegistryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@ComponentScan("by.aurorasoft.replicator")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ReplicationConfig {

    @Bean
    public ReplicatedServiceRegistry replicatedServiceRegistry(ReplicatedServiceRegistryFactory factory) {
        return factory.create();
    }

    @Bean
    public ReplicationProducerRegistry replicationProducerRegistry(ReplicationProducerRegistryFactory factory) {
        return factory.create();
    }

    @Bean
    public RetryTemplate replicationRetryTemplate(ReplicationRetryTemplateFactory factory) {
        return factory.create();
    }
}
