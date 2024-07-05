package by.aurorasoft.testapp.config;

import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
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
public class ReplicationConsumeConfig {

    @Bean
    public ReplicationConsumePipeline<ReplicatedPersonEntity, Long> personPipeline(@Value("${replication.consume.topic.person}") String topic,
                                                                                   ReplicatedPersonRepository repository) {
        return ReplicationConsumePipeline.<ReplicatedPersonEntity, Long>builder()
                .topic(topic)
                .idSerde(Long())
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(repository)
                .build();
    }

    @Bean
    public ReplicationConsumePipeline<ReplicatedAddressEntity, Long> addressPipeline(@Value("${replication.consume.topic.address}") String topic,
                                                                                     ReplicatedAddressRepository repository) {
        return ReplicationConsumePipeline.<ReplicatedAddressEntity, Long>builder()
                .topic(topic)
                .idSerde(Long())
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(repository)
                .build();
    }
}
