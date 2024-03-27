package by.aurorasoft.testapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME;

@Configuration
@EnableKafkaStreams
public class KafkaConfig {

    @Value("${spring.kafka.app-id}")
    private String applicationId;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean(name = DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration streamsConfiguration() {
        final Map<String, Object> configsByNames = Map.of(
                APPLICATION_ID_CONFIG, applicationId,
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ENABLE_AUTO_COMMIT_CONFIG, false
        );
        return new KafkaStreamsConfiguration(configsByNames);
    }
}
