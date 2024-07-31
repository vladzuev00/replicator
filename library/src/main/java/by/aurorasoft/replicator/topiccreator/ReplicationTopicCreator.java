package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import by.aurorasoft.replicator.event.PipelinesValidatedEvent;
import by.aurorasoft.replicator.event.ReplicationTopicsCreatedEvent;
import by.aurorasoft.replicator.factory.ReplicationTopicFactory;
import by.aurorasoft.replicator.registry.ReplicatedServiceRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationTopicCreator {
    private final ReplicatedServiceRegistry serviceRegistry;
    private final ReplicationTopicFactory topicFactory;
    private final KafkaAdmin kafkaAdmin;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener(PipelinesValidatedEvent.class)
    public void createTopics() {
        serviceRegistry.getServices()
                .stream()
                .map(this::getTopicConfig)
                .map(topicFactory::create)
                .forEach(kafkaAdmin::createOrModifyTopics);
        publishSuccessEvent();
    }

    private Topic getTopicConfig(Object service) {
        return service.getClass()
                .getAnnotation(ReplicatedRepository.class)
                .topicConfig();
    }

    private void publishSuccessEvent() {
        eventPublisher.publishEvent(new ReplicationTopicsCreatedEvent(this));
    }
}
