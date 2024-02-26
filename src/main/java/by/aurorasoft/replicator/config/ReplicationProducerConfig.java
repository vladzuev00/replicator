package by.aurorasoft.replicator.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static by.aurorasoft.replicator.util.ValidationUtil.withinIntervalOrElseThrow;
import static lombok.AccessLevel.PRIVATE;

@Component
@Getter
@EqualsAndHashCode
@ToString
@Builder
@FieldNameConstants(level = PRIVATE)
public final class ReplicationProducerConfig {
    private static final int MIN_VALID_BATCH_SIZE = 1;
    private static final int MAX_VALID_BATCH_SIZE = 10000;

    private static final int MIN_VALID_LINGER_MS = 1;
    private static final int MAX_VALID_LINGER_MS = 10000;

    private static final int MIN_VALID_DELIVERY_TIMEOUT_MS = 1;
    private static final int MAX_VALID_DELIVERY_TIMEOUT_MS = 1000000;

    private final int batchSize;
    private final int lingerMs;
    private final int deliveryTimeoutMs;

    public ReplicationProducerConfig(@Value("${kafka.entity-replication.producer.batch-size:#{10}}") final int batchSize,
                                     @Value("${kafka.entity-replication.producer.linger-ms:#{500}}") final int lingerMs,
                                     @Value("${kafka.entity-replication.producer.delivery-timeout-ms:#{100000}}") final int deliveryTimeoutMs) {
        validateBatchSize(batchSize);
        validateLingerMs(lingerMs);
        validateDeliveryTimeoutMs(deliveryTimeoutMs);
        this.batchSize = batchSize;
        this.lingerMs = lingerMs;
        this.deliveryTimeoutMs = deliveryTimeoutMs;
    }

    private static void validateBatchSize(final int batchSize) {
        withinIntervalOrElseThrow(batchSize, MIN_VALID_BATCH_SIZE, MAX_VALID_BATCH_SIZE, Fields.batchSize);
    }

    private static void validateLingerMs(final int lingerMs) {
        withinIntervalOrElseThrow(lingerMs, MIN_VALID_LINGER_MS, MAX_VALID_LINGER_MS, Fields.lingerMs);
    }

    private static void validateDeliveryTimeoutMs(final int deliveryTimeoutMs) {
        withinIntervalOrElseThrow(
                deliveryTimeoutMs,
                MIN_VALID_DELIVERY_TIMEOUT_MS,
                MAX_VALID_DELIVERY_TIMEOUT_MS,
                Fields.deliveryTimeoutMs
        );
    }
}
