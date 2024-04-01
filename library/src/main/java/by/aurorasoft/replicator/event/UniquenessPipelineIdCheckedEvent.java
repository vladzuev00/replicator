package by.aurorasoft.replicator.event;


import by.aurorasoft.replicator.validator.UniquePipelineIdValidator;
import org.springframework.context.ApplicationEvent;

public final class UniquenessPipelineIdCheckedEvent extends ApplicationEvent {

    public UniquenessPipelineIdCheckedEvent(final UniquePipelineIdValidator source) {
        super(source);
    }
}
