package by.aurorasoft.testapp.config;

import by.aurorasoft.replicator.consuming.ReplicationConsumingPipelineConfig;
import by.aurorasoft.testapp.crud.entity.ReplicatedPersonEntity;
import by.aurorasoft.testapp.crud.repository.ReplicatedPersonRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.kafka.common.serialization.Serdes.Long;

@Configuration
public class ReplicationConsumingConfig {

    @Value("${kafka.topic.sync-person.name}")
    private String topic;

    @Bean
    public ReplicationConsumingPipelineConfig<Long, ReplicatedPersonEntity> consumerConfig(final ReplicatedPersonRepository repository) {
        return ReplicationConsumingPipelineConfig.<Long, ReplicatedPersonEntity>builder()
                .topic(topic)
                .idSerde(Long())
                .replicationTypeReference(new TypeReference<>() {})
                .repository(repository)
                .build();
    }
}
