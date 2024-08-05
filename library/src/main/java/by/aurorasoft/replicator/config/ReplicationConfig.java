package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.factory.ReplicatedRepositoryRegistryFactory;
import by.aurorasoft.replicator.factory.ReplicationObjectMapperWrapperFactory;
import by.aurorasoft.replicator.factory.ReplicationProducerRegistryFactory;
import by.aurorasoft.replicator.factory.ReplicationRetryTemplateFactory;
import by.aurorasoft.replicator.mapperwrapper.ReplicationObjectMapperWrapper;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@ComponentScan("by.aurorasoft.replicator")
public class ReplicationConfig {

    @Bean
    public ReplicatedRepositoryRegistry replicatedRepositoryRegistry(ReplicatedRepositoryRegistryFactory factory) {
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

    @Bean
    public ReplicationObjectMapperWrapper replicationObjectMapperWrapper(ReplicationObjectMapperWrapperFactory factory,
                                                                         ObjectMapper source) {
        return factory.create(source);
    }
}
