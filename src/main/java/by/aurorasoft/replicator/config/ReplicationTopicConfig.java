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
public final class ReplicationTopicConfig {
    private static final int MIN_VALID_PARTITION_COUNT = 1;
    private static final int MAX_VALID_PARTITION_COUNT = 10;

    private static final int MIN_VALID_REPLICATION_FACTOR = 1;
    private static final int MAX_VALID_REPLICATION_FACTOR = 10;

    private final int partitionCount;
    private final int replicationFactor;

    public ReplicationTopicConfig(@Value("${kafka.entity-replication.topic.partition-count:#{1}}") final int partitionCount,
                                  @Value("${kafka.entity-replication.topic.replication-factor:#{3}}") final int replicationFactor) {
        validatePartitionCount(partitionCount);
        validateReplicationFactor(replicationFactor);
        this.partitionCount = partitionCount;
        this.replicationFactor = replicationFactor;
    }

    private static void validatePartitionCount(final int partitionCount) {
        withinIntervalOrElseThrow(
                partitionCount,
                MIN_VALID_PARTITION_COUNT,
                MAX_VALID_PARTITION_COUNT,
                Fields.partitionCount
        );
    }

    private static void validateReplicationFactor(final int replicationFactor) {
        withinIntervalOrElseThrow(
                replicationFactor,
                MIN_VALID_REPLICATION_FACTOR,
                MAX_VALID_REPLICATION_FACTOR,
                Fields.replicationFactor
        );
    }
}
