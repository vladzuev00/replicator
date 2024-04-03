package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.exception.PerhapsRelationNotDeliveredYetException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

@Configuration
public class ReplicationRetryConfig {

    @Value("${replication.consume.retry.time-lapse-ms}")
    private long timeLapseMs;

    @Value("${replication.consume.retry.max-attempts}")
    private int maxAttempts;

    @Bean("replicationRetryTemplate")
    public RetryTemplate retryTemplate() {
        return new RetryTemplateBuilder()
                .fixedBackoff(timeLapseMs)
                .maxAttempts(maxAttempts)
                .retryOn(PerhapsRelationNotDeliveredYetException.class)
                .build();
    }
}
