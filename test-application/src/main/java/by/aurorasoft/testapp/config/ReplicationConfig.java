package by.aurorasoft.testapp.config;

import by.aurorasoft.replicator.annotation.EnableReplication;
import by.aurorasoft.replicator.model.setting.ReplicationConsumeSetting;
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
public class ReplicationConfig {

    @Bean
    public ReplicationConsumeSetting<ReplicatedPersonEntity, Long> personReplicationConsumeSetting(@Value("${replication.consume.topic.person}") String topic,
                                                                                                   ReplicatedPersonRepository repository) {
        return new ReplicationConsumeSetting<>(
                topic,
                repository,
                new LongDeserializer(),
                new TypeReference<>() {
                }
        );
    }

    @Bean
    public ReplicationConsumeSetting<ReplicatedAddressEntity, Long> addressReplicationConsumeSetting(@Value("${replication.consume.topic.address}") String topic,
                                                                                                     ReplicatedAddressRepository repository) {
        return new ReplicationConsumeSetting<>(
                topic,
                repository,
                new LongDeserializer(),
                new TypeReference<>() {
                }
        );
    }
}
