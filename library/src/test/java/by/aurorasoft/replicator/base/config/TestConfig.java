package by.aurorasoft.replicator.base.config;

import by.aurorasoft.replicator.config.ReplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaAdmin;

import static org.mockito.Mockito.mock;

@Configuration
@Import(ReplicationConfig.class)
public class TestConfig {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return mock(KafkaAdmin.class);
    }
}
