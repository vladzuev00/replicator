package by.aurorasoft.replicator.base.config;

import by.aurorasoft.replicator.annotation.EnableReplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonViewModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import static org.mockito.Mockito.mock;

@Configuration
@EnableReplication
public class TestConfig {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return mock(KafkaAdmin.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JsonViewModule());
    }
}
