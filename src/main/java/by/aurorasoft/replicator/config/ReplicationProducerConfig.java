package by.aurorasoft.replicator.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ReplicationProducerConfig {
    private final int batchSize;
    private final int lingerMs;
    private final int deliveryTimeoutMs;

    public ReplicationProducerConfig(@Value("${kafka.entity-replication.producer.batch-size}") final int batchSize,
                                     @Value("${kafka.entity-replication.producer.linger-ms}") final int lingerMs,
                                     @Value("${kafka.entity-replication.producer.delivery-timeout-ms}") final int deliveryTimeoutMs) {
        this.batchSize = batchSize;
        this.lingerMs = lingerMs;
        this.deliveryTimeoutMs = deliveryTimeoutMs;
    }
}
