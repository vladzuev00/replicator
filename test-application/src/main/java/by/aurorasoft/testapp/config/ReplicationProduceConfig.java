package by.aurorasoft.testapp.config;

import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.repository.AddressRepository;
import by.aurorasoft.testapp.crud.repository.PersonRepository;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReplicationProduceConfig {

    @Bean
    public LongSerializer longSerializer() {
        return new LongSerializer();
    }

    @Bean
    public ReplicationProduceSetting<PersonEntity, Long> personReplicationProduceSetting(@Value("${replication.consume.topic.person}") String topic,
                                                                                         PersonRepository repository) {
        return ReplicationProduceSetting.<PersonEntity, Long>builder()
                .topic(topic)
                .repository(repository)
                .idSerializer(longSerializer())
                .build();
    }

    @Bean
    public ReplicationProduceSetting<AddressEntity, Long> addressReplicationProduceSetting(@Value("${replication.consume.topic.address}") String topic,
                                                                                           AddressRepository repository) {
        return ReplicationProduceSetting.<AddressEntity, Long>builder()
                .topic(topic)
                .repository(repository)
                .idSerializer(longSerializer())
                .build();
    }
}
