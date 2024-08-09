package by.aurorasoft.testapp.config;

import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReplicationProduceConfig {

//    @Bean
//    public ReplicationProduceSetting<PersonEntity, Long> personReplicationProduceSetting(@Value("${replication.consume.topic.person}") String topic,
//                                                                                         PersonRepository repository) {
//        return new ReplicationProduceSetting<>(topic, repository, )
//    }
}
