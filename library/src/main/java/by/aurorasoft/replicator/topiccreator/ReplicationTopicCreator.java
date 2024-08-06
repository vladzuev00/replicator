package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.event.ReplicationComponentsValidatedEvent;
import by.aurorasoft.replicator.event.ReplicationTopicsCreatedEvent;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationTopicCreator {
    private final ReplicatedRepositoryRegistry repositoryRegistry;
    private final KafkaAdmin kafkaAdmin;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener(ReplicationComponentsValidatedEvent.class)
    public void createTopics() {
//        repositoryRegistry.getRepositories()
//                .stream()
//                .map(this::getTopicConfig)
//                .map(this::createNewTopic)
//                .forEach(kafkaAdmin::createOrModifyTopics);
        publishSuccessEvent();
    }

    private void publishSuccessEvent() {
        eventPublisher.publishEvent(new ReplicationTopicsCreatedEvent(this));
    }
}
