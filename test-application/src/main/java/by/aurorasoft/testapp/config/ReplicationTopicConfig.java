package by.aurorasoft.testapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReplicationTopicConfig {
    private static final int TOPIC_PARTITIONS_COUNT = 1;
    private static final short TOPIC_REPLICATION_FACTOR = 1;

    @Bean
    public NewTopic personReplicationTopic(@Value("${replication.consume.topic.person}") String name) {
        return new NewTopic(name, TOPIC_PARTITIONS_COUNT, TOPIC_REPLICATION_FACTOR);
    }

    @Bean
    public NewTopic addressReplicationTopic(@Value("${replication.consume.topic.address}") String name) {
        return new NewTopic(name, TOPIC_PARTITIONS_COUNT, TOPIC_REPLICATION_FACTOR);
    }
}
