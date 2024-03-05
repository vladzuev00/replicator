package by.aurorasoft.testapp.config;

import by.aurorasoft.replicator.consumer.KafkaReplicationConsumerConfig;
import by.aurorasoft.testapp.crud.dto.ReplicatedPerson;
import by.aurorasoft.testapp.crud.service.ReplicatedPersonService;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReplicationConsumingConfig {

    @Value("${kafka.topic.sync-person.consumer.group-id}")
    private String groupId;

    @Value("${kafka.topic.sync-person.name}")
    private String topic;

    @Bean
    public KafkaReplicationConsumerConfig<Long, ReplicatedPerson> consumerConfig(final ReplicatedPersonService service) {
        return new KafkaReplicationConsumerConfig<>(groupId, topic, new LongDeserializer(), service);
    }
}
