package by.aurorasoft.replicator.event;

import by.aurorasoft.replicator.validator.UniquePipelineTopicValidator;
import org.springframework.context.ApplicationEvent;

public final class PipelinesValidatedEvent extends ApplicationEvent {

    public PipelinesValidatedEvent(UniquePipelineTopicValidator source) {
        super(source);
    }
}
