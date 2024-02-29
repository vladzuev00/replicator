package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import org.springframework.stereotype.Component;

import static by.aurorasoft.replicator.util.ValidationUtil.withinIntervalOrElseThrow;

@Component
public final class TopicConfigValidator {
    private static final int MIN_VALID_PARTITION_COUNT = 1;
    private static final int MAX_VALID_PARTITION_COUNT = 10;
    private static final String PROPERTY_NAME_PARTITION_COUNT = "partitionCount";

    private static final int MIN_VALID_REPLICATION_FACTOR = 1;
    private static final int MAX_VALID_REPLICATION_FACTOR = 10;
    private static final String PROPERTY_NAME_REPLICATION_FACTOR = "replicationFactor";

    public void validate(final TopicConfig config) {
        validateTopicName(config.name());
        validatePartitionCount(config.partitionCount());
        validateReplicationFactor(config.replicationFactor());
    }

    private static void validateTopicName(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Topic name should not be blank");
        }
    }

    private static void validatePartitionCount(final int partitionCount) {
        withinIntervalOrElseThrow(
                partitionCount,
                MIN_VALID_PARTITION_COUNT,
                MAX_VALID_PARTITION_COUNT,
                PROPERTY_NAME_PARTITION_COUNT
        );
    }

    private static void validateReplicationFactor(final int replicationFactor) {
        withinIntervalOrElseThrow(
                replicationFactor,
                MIN_VALID_REPLICATION_FACTOR,
                MAX_VALID_REPLICATION_FACTOR,
                PROPERTY_NAME_REPLICATION_FACTOR
        );
    }
}
