package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationConsumeSetting;

public final class ReplicationConsumeSettingUniqueTopicValidator
        extends UniquePropertyValidator<ReplicationConsumeSetting<?, ?>, String> {
    private static final String VIOLATION_MESSAGE = "Replication consuming setting's topics should be unique";

    public ReplicationConsumeSettingUniqueTopicValidator() {
        super(VIOLATION_MESSAGE);
    }

    @Override
    protected String getProperty(ReplicationConsumeSetting<?, ?> setting) {
        return setting.getTopic();
    }
}
