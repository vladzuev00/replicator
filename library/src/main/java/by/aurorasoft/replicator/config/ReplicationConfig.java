package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.holder.KafkaReplicationProducerHolder;
import by.aurorasoft.replicator.producer.KafkaReplicationProducerHolderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("by.aurorasoft.replicator")
public class ReplicationConfig {

    @Bean
    public KafkaReplicationProducerHolder replicationProducerHolder(final KafkaReplicationProducerHolderFactory factory) {
        return factory.create();
    }
}
