package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationConsumeSetting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ReplicationConsumeSettingUniqueTopicValidatorTest {
    private final ReplicationConsumeSettingUniqueTopicValidator validator = new ReplicationConsumeSettingUniqueTopicValidator();

    @Test
    public void propertyShouldBeGot() {
        String givenTopicName = "test-topic";
        ReplicationConsumeSetting<?, ?> givenSetting = mock(ReplicationConsumeSetting.class);

        when(givenSetting.getTopic()).thenReturn(givenTopicName);

        String actual = validator.getProperty(givenSetting);
        assertSame(givenTopicName, actual);
    }
}
