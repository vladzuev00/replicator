package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.producer.ReplicationProducerHolderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("by.aurorasoft.replicator")
public class ReplicationConfig {

    @Bean
    public ReplicationProducerHolder replicationProducerHolder(final ReplicationProducerHolderFactory factory) {
        return factory.create();
    }
}
