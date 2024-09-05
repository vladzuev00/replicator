package by.aurorasoft.replicator.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@lombok.Value
public class ReplicationRetryConsumeProperty {
    long timeLapseMs;
    int maxAttempts;

    public ReplicationRetryConsumeProperty(@Value("${replication.consume.retry.time-lapse-ms:2000}") long timeLapseMs,
                                           @Value("${replication.consume.retry.max-attempts:50}") int maxAttempts) {
        this.timeLapseMs = timeLapseMs;
        this.maxAttempts = maxAttempts;
    }
}
