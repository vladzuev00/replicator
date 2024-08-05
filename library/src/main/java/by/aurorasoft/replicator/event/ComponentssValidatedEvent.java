package by.aurorasoft.replicator.event;

import by.aurorasoft.replicator.validator.UniquePipelineTopicValidator;
import org.springframework.context.ApplicationEvent;

public final class ComponentssValidatedEvent extends ApplicationEvent {

    public ComponentssValidatedEvent(UniquePipelineTopicValidator source) {
        super(source);
    }
}
