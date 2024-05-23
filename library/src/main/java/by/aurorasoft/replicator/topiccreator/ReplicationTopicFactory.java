package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.config.TopicBuilder.name;

@Component
public final class ReplicationTopicFactory {

    public NewTopic create(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return name(getTopicName(service))
                .partitions(getTopicPartitionCount(service))
                .replicas(getTopicReplicationFactor(service))
                .build();
    }

    private static String getTopicName(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return getTopicConfig(service).name();
    }

    private static int getTopicPartitionCount(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return getTopicConfig(service).partitionCount();
    }

    private static short getTopicReplicationFactor(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return getTopicConfig(service).replicationFactor();
    }

    private static TopicConfig getTopicConfig(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return service.getClass()
                .getAnnotation(ReplicatedService.class)
                .topicConfig();
    }
}
