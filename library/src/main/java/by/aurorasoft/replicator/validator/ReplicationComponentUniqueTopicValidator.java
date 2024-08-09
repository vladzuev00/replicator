package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationComponentSetting;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationComponentUniqueTopicValidator extends ReplicationComponentUniquePropertyValidator<String> {
    private static final String VIOLATION_MESSAGE = "Duplicated topics were found";

    public ReplicationComponentUniqueTopicValidator() {
        super(VIOLATION_MESSAGE);
    }

    @Override
    protected <S extends ReplicationComponentSetting<?, ?>> String getProperty(S setting) {
        return setting.getTopic();
    }
}
