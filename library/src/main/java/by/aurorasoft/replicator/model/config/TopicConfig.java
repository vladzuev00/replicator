package by.aurorasoft.replicator.model.config;

import lombok.Value;

@Value
public class TopicConfig {
    String name;
    int partitionCount;
    short replicationFactor;
}
