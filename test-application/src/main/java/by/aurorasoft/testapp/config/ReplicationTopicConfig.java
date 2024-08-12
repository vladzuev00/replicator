package by.aurorasoft.testapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//TODO: refactor
@Configuration
public class ReplicationTopicConfig {

    @Bean
    public NewTopic personReplicationTopic(@Value("${replication.consume.topic.person}") String name) {
        return new NewTopic(name, 1, (short) 1);
    }

    @Bean
    public NewTopic addressReplicationTopic(@Value("${replication.consume.topic.address}") String name) {
        return new NewTopic(name, 1, (short) 1);
    }
}
