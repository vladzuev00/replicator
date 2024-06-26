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
public class ReplicationConsumingConfig {

    @Value("${kafka.topic.sync-person.name}")
    private String personTopic;

    @Value("${replication.consume.pipeline.id.person}")
    private String personPipelineId;

    @Value("${kafka.topic.sync-address.name}")
    private String addressTopic;

    @Value("${replication.consume.pipeline.id.address}")
    private String addressPipelineId;

    @Bean
    public ReplicationConsumePipeline<ReplicatedPersonEntity, Long> personPipeline(
            final ReplicatedPersonRepository repository
    ) {
        return ReplicationConsumePipeline.<ReplicatedPersonEntity, Long>builder()
                .id(personPipelineId)
                .topic(personTopic)
                .idSerde(Long())
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(repository)
                .build();
    }

    @Bean
    public ReplicationConsumePipeline<ReplicatedAddressEntity, Long> addressPipeline(
            final ReplicatedAddressRepository repository
    ) {
        return ReplicationConsumePipeline.<ReplicatedAddressEntity, Long>builder()
                .id(addressPipelineId)
                .topic(addressTopic)
                .idSerde(Long())
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(repository)
                .build();
    }
}
