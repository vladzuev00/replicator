package by.aurorasoft.replicator.base;

import by.aurorasoft.replicator.config.ReplicationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AbstractTest.TestConfiguration.class)
public abstract class AbstractTest {

    @Import(ReplicationConfig.class)
    static class TestConfiguration {

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public KafkaAdmin kafkaAdmin() {
            return mock(KafkaAdmin.class);
        }
    }
}
