package by.aurorasoft.replicator.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ReplicationTopicConfig {
    private final int partitionCount;
    private final int replicationFactor;

    public ReplicationTopicConfig(@Value("${kafka.entity-replication.topic.partition-count}") final int partitionCount,
                                  @Value("${kafka.entity-replication.topic.replication-factor}") final int replicationFactor) {
        this.partitionCount = partitionCount;
        this.replicationFactor = replicationFactor;
    }
}
