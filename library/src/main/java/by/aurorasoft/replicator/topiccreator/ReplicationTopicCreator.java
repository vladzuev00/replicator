package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.holder.ReplicatedServiceHolder;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationTopicCreator {
    private final ReplicatedServiceHolder replicatedServiceHolder;
    private final KafkaAdmin kafkaAdmin;
    private final ReplicationTopicConfig config;

    @EventListener(ContextRefreshedEvent.class)
    public void createTopics() {
        replicatedServiceHolder.getServices().forEach(this::createTopic);
    }

    private void createTopic(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        final NewTopic topic = TopicBuilder.name(getTopicName(service))
                .partitions(config.getPartitionCount())
                .replicas(config.getReplicationFactor())
                .build();
        kafkaAdmin.createOrModifyTopics(topic);
    }

    private static String getTopicName(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return service.getClass().getAnnotation(ReplicatedService.class).topicName();
    }
}
