package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import org.springframework.stereotype.Component;

@Component
public final class ReplicatedServiceUniqueTopicValidator extends UniquePropertyValidator<Object, String> {
    private static final String VIOLATION_MESSAGE = "Replicated service's topics should be unique";

    public ReplicatedServiceUniqueTopicValidator() {
        super(VIOLATION_MESSAGE);
    }

    @Override
    protected String getProperty(Object service) {
        return service.getClass()
                .getAnnotation(ReplicatedService.class)
                .topicConfig()
                .name();
    }
}
