package by.aurorasoft.replicator.event;


import by.aurorasoft.replicator.validator.UniquePipelineIdValidator;
import org.springframework.context.ApplicationEvent;

public final class PipelineIdUniqueConstraintCheckedEvent extends ApplicationEvent {

    public PipelineIdUniqueConstraintCheckedEvent(final UniquePipelineIdValidator source) {
        super(source);
    }
}
