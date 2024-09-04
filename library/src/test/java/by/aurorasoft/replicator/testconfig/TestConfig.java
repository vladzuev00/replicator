package by.aurorasoft.replicator.testconfig;

import by.aurorasoft.replicator.annotation.EnableReplication;
import by.aurorasoft.replicator.testcrud.TestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        return new ObjectMapper();
    }

    @Bean
    public TestRepository testRepository() {
        return mock(TestRepository.class);
    }
}
