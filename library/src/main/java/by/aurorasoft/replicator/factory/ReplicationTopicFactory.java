package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.config.TopicBuilder.name;

@Component
public final class ReplicationTopicFactory {

    public NewTopic create(Object service) {
        return name(getTopicName(service))
                .partitions(getTopicPartitionCount(service))
                .replicas(getTopicReplicationFactor(service))
                .build();
    }

    private String getTopicName(Object service) {
        return getTopicConfig(service).name();
    }

    private int getTopicPartitionCount(Object service) {
        return getTopicConfig(service).partitionCount();
    }

    private short getTopicReplicationFactor(Object service) {
        return getTopicConfig(service).replicationFactor();
    }

    private TopicConfig getTopicConfig(Object service) {
        return service.getClass()
                .getAnnotation(ReplicatedService.class)
                .topicConfig();
    }
}
