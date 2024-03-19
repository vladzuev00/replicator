package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.holder.ReplicatedServiceHolder;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationTopicCreator {
    private final ReplicatedServiceHolder serviceHolder;
    private final KafkaAdmin kafkaAdmin;

    @PostConstruct
    public void createTopics() {
        serviceHolder.getServices().forEach(this::createTopic);
    }

    private void createTopic(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        final NewTopic topic = TopicBuilder.name(getTopicName(service))
                .partitions(getTopicPartitionCount(service))
                .replicas(getTopicReplicationFactor(service))
                .build();
        kafkaAdmin.createOrModifyTopics(topic);
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
