package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.config.TopicBuilder.name;

@Component
public final class ReplicationTopicFactory {

    public NewTopic create(Topic config) {
        return name(config.name())
                .partitions(config.partitionCount())
                .replicas(config.replicationFactor())
                .build();
    }
}
