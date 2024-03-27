package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.factory.ReplicationProducerHolderFactory;
import by.aurorasoft.replicator.factory.ReplicationTopicRetryConfigFactory;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;

import java.util.List;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("by.aurorasoft.replicator")
public class ReplicationConfig {

    @Bean
    public ReplicationProducerHolder replicationProducerHolder(final ReplicationProducerHolderFactory factory) {
        return factory.create();
    }

//    @Bean
//    public List<RetryTopicConfiguration> retryTopicConfigs(final ReplicationTopicRetryConfigFactory configFactory,
//                                                           final ReplicationProducerHolder producerHolder) {
//        return producerHolder.getProducers().stream().map(configFactory::create).toList();
//    }
}
