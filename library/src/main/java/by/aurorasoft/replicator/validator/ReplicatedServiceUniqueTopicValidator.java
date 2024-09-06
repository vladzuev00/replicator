package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;

public final class ReplicatedServiceUniqueTopicValidator extends UniquePropertyValidator<ReplicatedService, String> {
    private static final String VIOLATION_MESSAGE = "Replicated service's topics should be unique";

    public ReplicatedServiceUniqueTopicValidator() {
        super(VIOLATION_MESSAGE);
    }

    @Override
    protected String getProperty(ReplicatedService service) {
        return service.topicConfig().name();
    }
}
