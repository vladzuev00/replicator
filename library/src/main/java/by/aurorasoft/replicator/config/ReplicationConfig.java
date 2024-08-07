package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.factory.DeleteReplicationProducerRegistryFactory;
import by.aurorasoft.replicator.factory.ReplicationRetryTemplateFactory;
import by.aurorasoft.replicator.factory.SaveReplicationProducerRegistryFactory;
import by.aurorasoft.replicator.registry.replicationproducer.DeleteReplicationProducerRegistry;
import by.aurorasoft.replicator.registry.replicationproducer.SaveReplicationProducerRegistry;
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
    public SaveReplicationProducerRegistry saveReplicationProducerRegistry(SaveReplicationProducerRegistryFactory factory) {
        return factory.create();
    }

    @Bean
    public DeleteReplicationProducerRegistry deleteReplicationProducerRegistry(DeleteReplicationProducerRegistryFactory factory) {
        return factory.create();
    }
}
