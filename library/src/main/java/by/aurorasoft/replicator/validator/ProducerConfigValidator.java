package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import org.springframework.stereotype.Component;

import static by.aurorasoft.replicator.util.ValidationUtil.withinIntervalOrElseThrow;

@Component
public final class ProducerConfigValidator {
    private static final int MIN_VALID_BATCH_SIZE = 1;
    private static final int MAX_VALID_BATCH_SIZE = 10000;
    private static final String PROPERTY_NAME_BATCH_SIZE = "batchSize";

    private static final int MIN_VALID_LINGER_MS = 1;
    private static final int MAX_VALID_LINGER_MS = 10000;
    private static final String PROPERTY_NAME_LINGER_MS = "lingerMs";

    private static final int MIN_VALID_DELIVERY_TIMEOUT_MS = 1;
    private static final int MAX_VALID_DELIVERY_TIMEOUT_MS = 1000000;
    private static final String PROPERTY_NAME_DELIVERY_TIMEOUT_MS = "deliveryTimeoutMs";

    public void validate(final ProducerConfig config) {
        validateBatchSize(config.batchSize());
        validateLingerMs(config.lingerMs());
        validateDeliveryTimeoutMs(config.deliveryTimeoutMs());
    }

    private static void validateBatchSize(final int batchSize) {
        withinIntervalOrElseThrow(
                batchSize,
                MIN_VALID_BATCH_SIZE,
                MAX_VALID_BATCH_SIZE,
                PROPERTY_NAME_BATCH_SIZE
        );
    }

    private static void validateLingerMs(final int lingerMs) {
        withinIntervalOrElseThrow(
                lingerMs,
                MIN_VALID_LINGER_MS,
                MAX_VALID_LINGER_MS,
                PROPERTY_NAME_LINGER_MS
        );
    }

    private static void validateDeliveryTimeoutMs(final int deliveryTimeoutMs) {
        withinIntervalOrElseThrow(
                deliveryTimeoutMs,
                MIN_VALID_DELIVERY_TIMEOUT_MS,
                MAX_VALID_DELIVERY_TIMEOUT_MS,
                PROPERTY_NAME_DELIVERY_TIMEOUT_MS
        );
    }
}
