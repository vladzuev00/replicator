package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.event.PipelinesValidatedEvent;
import by.aurorasoft.replicator.event.ReplicationTopicsCreatedEvent;
import by.aurorasoft.replicator.holder.service.ReplicatedServiceHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationTopicCreator {
    private final ReplicatedServiceHolder serviceHolder;
    private final ReplicationTopicFactory topicFactory;
    private final KafkaAdmin kafkaAdmin;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener(PipelinesValidatedEvent.class)
    public void createTopics() {
        serviceHolder.getServices()
                .stream()
                .map(topicFactory::create)
                .forEach(kafkaAdmin::createOrModifyTopics);
        publishSuccessEvent();
    }

    private void publishSuccessEvent() {
        eventPublisher.publishEvent(new ReplicationTopicsCreatedEvent(this));
    }
}
