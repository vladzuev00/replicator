package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.holder.KafkaReplicationProducerHolder;
import by.aurorasoft.replicator.model.TransportableReplication;
import by.aurorasoft.replicator.producer.KafkaProducerReplicationHolderFactory;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("by.aurorasoft.replicator")
public class ReplicationConfig {

    @Bean
    public Schema replicationSchema() {
        return ReflectData.get().getSchema(TransportableReplication.class);
    }

    @Bean
    public KafkaReplicationProducerHolder replicationProducerHolder(final KafkaProducerReplicationHolderFactory factory) {
        return factory.create();
    }
}
