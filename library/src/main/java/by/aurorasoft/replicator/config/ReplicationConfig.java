package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.factory.retrytemplate.ReplicationRetryTemplateFactory;
import by.aurorasoft.replicator.factory.registry.ReplicationProducerRegistryFactory;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonViewModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("by.aurorasoft.replicator")
public class ReplicationConfig {

    public ReplicationConfig(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JsonViewModule());
    }

    @Bean
    public RetryTemplate replicationRetryTemplate(ReplicationRetryTemplateFactory factory) {
        return factory.create();
    }

    @Bean
    public ReplicationProducerRegistry replicationProducerRegistry(ReplicationProducerRegistryFactory factory) {
        return factory.create();
    }
}
