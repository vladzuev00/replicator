package by.aurorasoft.replicator.event;


import by.aurorasoft.replicator.validator.PipelineIdUniqueConstraintValidator;
import org.springframework.context.ApplicationEvent;

public final class UniquenessPipelineIdCheckedEvent extends ApplicationEvent {

    public UniquenessPipelineIdCheckedEvent(final PipelineIdUniqueConstraintValidator source) {
        super(source);
    }
}
