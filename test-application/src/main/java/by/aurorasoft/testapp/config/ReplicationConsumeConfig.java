package by.aurorasoft.testapp.config;

import by.aurorasoft.replicator.annotation.EnableReplication;
import by.aurorasoft.replicator.model.config.component.ReplicationConsumer;
import by.aurorasoft.testapp.crud.entity.ReplicatedAddressEntity;
import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import by.aurorasoft.testapp.crud.repository.ReplicatedAddressRepository;
import by.aurorasoft.testapp.crud.repository.ReplicatedPersonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableReplication
public class ReplicationConsumeConfig {

    @Bean
    public ReplicationConsumer<ReplicatedPersonEntity, Long> personPipeline(@Value("${replication.consume.topic.person}") String topic,
                                                                            ReplicatedPersonRepository repository) {
        return new ReplicationConsumer<>(
                topic,
                new LongDeserializer(),
                new TypeReference<>() {
                },
                repository
        );
    }

    @Bean
    public ReplicationConsumer<ReplicatedAddressEntity, Long> addressPipeline(@Value("${replication.consume.topic.address}") String topic,
                                                                              ReplicatedAddressRepository repository) {
        return new ReplicationConsumer<>(
                topic,
                new LongDeserializer(),
                new TypeReference<>() {
                },
                repository
        );
    }
}
