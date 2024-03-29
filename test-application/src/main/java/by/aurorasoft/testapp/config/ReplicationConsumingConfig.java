package by.aurorasoft.testapp.config;

import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import by.aurorasoft.testapp.crud.entity.ReplicatedAddressEntity;
import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import by.aurorasoft.testapp.crud.repository.ReplicatedAddressRepository;
import by.aurorasoft.testapp.crud.repository.ReplicatedPersonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.kafka.common.serialization.Serdes.Long;

@Configuration
public class ReplicationConsumingConfig {

    @Value("${kafka.topic.sync-person.name}")
    private String personTopic;

    @Value("${kafka.topic.sync-address.name}")
    private String addressTopic;

    @Bean
    public ReplicationConsumePipeline<Long, ReplicatedPersonEntity> personPipeline(
            final ReplicatedPersonRepository repository
    ) {
        return ReplicationConsumePipeline.<Long, ReplicatedPersonEntity>builder()
                .topic(personTopic)
                .idSerde(Long())
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(repository)
                .build();
    }

    @Bean
    public ReplicationConsumePipeline<Long, ReplicatedAddressEntity> addressPipeline(
            final ReplicatedAddressRepository repository
    ) {
        return ReplicationConsumePipeline.<Long, ReplicatedAddressEntity>builder()
                .topic(addressTopic)
                .idSerde(Long())
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(repository)
                .build();
    }
}
