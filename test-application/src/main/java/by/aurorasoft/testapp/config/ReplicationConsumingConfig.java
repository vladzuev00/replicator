package by.aurorasoft.testapp.config;

import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumer;
import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import by.aurorasoft.testapp.crud.repository.ReplicatedPersonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReplicationConsumingConfig {

    @Value("${kafka.topic.sync-person.consumer.group-id}")
    private String groupId;

    @Value("${kafka.topic.sync-person.name}")
    private String topic;

    @Bean
    public ReplicationConsumer<Long, ReplicatedPersonEntity> consumerConfig(final ReplicatedPersonRepository repository,
                                                                            //TODO: remove publisher
                                                                            final ApplicationEventPublisher publisher) {
        return new ReplicationConsumer<>(
                groupId,
                topic,
                new LongDeserializer(),
                new TypeReference<>() {
                },
                repository,
                publisher
        );
    }
}
