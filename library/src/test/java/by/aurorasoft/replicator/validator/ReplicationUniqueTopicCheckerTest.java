package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationSetting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ReplicationUniqueTopicCheckerTest {
    private final ReplicationUniqueTopicChecker checker = new ReplicationUniqueTopicChecker();

    @Test
    public void propertyShouldBeGot() {
        String givenTopic = "test-topic";
        TestReplicationSetting givenSetting = new TestReplicationSetting(givenTopic);

        String actual = checker.getProperty(givenSetting);
        assertSame(givenTopic, actual);
    }

    private static final class TestReplicationSetting extends ReplicationSetting<Object, Object> {

        public TestReplicationSetting(String topic) {
            super(topic, null);
        }
    }
}
