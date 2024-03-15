package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
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

    //TODO: replace by @PostConstruct
    @EventListener(ContextRefreshedEvent.class)
    public void createTopics() {
        replicatedServiceHolder.getServices().forEach(this::createTopic);
    }

    private void createTopic(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        final TopicConfig config = getTopicConfig(service);
        final NewTopic topic = TopicBuilder.name(config.name())
                .partitions(config.partitionCount())
                .replicas(config.replicationFactor())
                .build();
        kafkaAdmin.createOrModifyTopics(topic);
    }

    private static TopicConfig getTopicConfig(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return service.getClass().getAnnotation(ReplicatedService.class).topicConfig();
    }
}
