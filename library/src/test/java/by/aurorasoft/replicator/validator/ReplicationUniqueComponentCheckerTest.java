package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationSetting;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ReplicationUniqueComponentCheckerTest {
    private final ReplicationUniqueComponentChecker<String> checker = new TestReplicationUniqueComponentChecker();

    @Test
    public void componentsShouldBeCheckedWithoutViolationException() {
        List<TestReplicationSetting> givenSettings = List.of(
                new TestReplicationSetting("first-topic"),
                new TestReplicationSetting("second-topic"),
                new TestReplicationSetting("third-topic"),
                new TestReplicationSetting("fourth-topic"),
                new TestReplicationSetting("fifth-topic")
        );

        checker.check(givenSettings);
    }

    @Test
    public void componentsShouldBeCheckedWithViolationException() {
        List<TestReplicationSetting> givenSettings = List.of(
                new TestReplicationSetting("first-topic"),
                new TestReplicationSetting("second-topic"),
                new TestReplicationSetting("third-topic"),
                new TestReplicationSetting("fourth-topic"),
                new TestReplicationSetting("first-topic")
        );

        assertThrows(IllegalStateException.class, () -> checker.check(givenSettings));
    }

    private static final class TestReplicationSetting extends ReplicationSetting<Object, Object> {

        public TestReplicationSetting(String topic) {
            super(topic, null);
        }
    }

    private static final class TestReplicationUniqueComponentChecker extends ReplicationUniqueComponentChecker<String> {
        private static final String VIOLATION_MESSAGE = "test-violation-message";

        public TestReplicationUniqueComponentChecker() {
            super(VIOLATION_MESSAGE);
        }

        @Override
        protected <S extends ReplicationSetting<?, ?>> String getProperty(S setting) {
            return setting.getTopic();
        }
    }
}