package by.aurorasoft.replicator.event;


import by.aurorasoft.replicator.validator.PipelineValidator;
import org.springframework.context.ApplicationEvent;

public final class PipelinesValidatedEvent extends ApplicationEvent {

    public PipelinesValidatedEvent(PipelineValidator source) {
        super(source);
    }
}
