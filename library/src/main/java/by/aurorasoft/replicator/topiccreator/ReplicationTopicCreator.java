package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.event.ComponentssValidatedEvent;
import by.aurorasoft.replicator.event.ReplicationTopicsCreatedEvent;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import static by.aurorasoft.replicator.util.AnnotationUtil.getAnnotation;

@Component
@RequiredArgsConstructor
public final class ReplicationTopicCreator {
    private final ReplicatedRepositoryRegistry repositoryRegistry;
    private final KafkaAdmin kafkaAdmin;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener(ComponentssValidatedEvent.class)
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
