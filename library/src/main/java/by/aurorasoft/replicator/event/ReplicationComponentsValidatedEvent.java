package by.aurorasoft.replicator.event;

import by.aurorasoft.replicator.validator.UniquePipelineTopicValidator;
import org.springframework.context.ApplicationEvent;

public final class ReplicationComponentsValidatedEvent extends ApplicationEvent {

    public ReplicationComponentsValidatedEvent(UniquePipelineTopicValidator source) {
        super(source);
    }
}
