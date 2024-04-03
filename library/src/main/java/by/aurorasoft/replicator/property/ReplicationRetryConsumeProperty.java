package by.aurorasoft.replicator.property;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@EqualsAndHashCode
@ToString
public final class ReplicationRetryConsumeProperty {

    @Value("${replication.consume.retry.time-lapse-ms}")
    private long timeLapseMs;

    @Value("${replication.consume.retry.max-attempts}")
    private int maxAttempts;

    public ReplicationRetryConsumeProperty(@Value("${replication.consume.retry.time-lapse-ms}") final long timeLapseMs,
                                           @Value("${replication.consume.retry.max-attempts}") final int maxAttempts) {
        this.timeLapseMs = timeLapseMs;
        this.maxAttempts = maxAttempts;
    }
}
