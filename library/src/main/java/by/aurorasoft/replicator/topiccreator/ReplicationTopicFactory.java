package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.config.TopicBuilder.name;

@Component
public final class ReplicationTopicFactory {

    public NewTopic create(final Object service) {
        return name(getTopicName(service))
                .partitions(getTopicPartitionCount(service))
                .replicas(getTopicReplicationFactor(service))
                .build();
    }

    private String getTopicName(final Object service) {
        return getTopicConfig(service).name();
    }

    private int getTopicPartitionCount(final Object service) {
        return getTopicConfig(service).partitionCount();
    }

    private short getTopicReplicationFactor(final Object service) {
        return getTopicConfig(service).replicationFactor();
    }

    private TopicConfig getTopicConfig(final Object service) {
        return service.getClass()
                .getAnnotation(ReplicatedService.class)
                .topicConfig();
    }
}
