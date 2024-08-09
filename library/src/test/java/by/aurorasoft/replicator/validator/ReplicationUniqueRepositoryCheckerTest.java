package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.model.setting.ReplicationSetting;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

public final class ReplicationUniqueRepositoryCheckerTest {
    private final ReplicationUniqueRepositoryChecker checker = new ReplicationUniqueRepositoryChecker();

    @Test
    public void propertyShouldBeGot() {
        @SuppressWarnings("unchecked") JpaRepository<Object, Object> givenRepository = mock(JpaRepository.class);
        TestReplicationSetting givenSetting = new TestReplicationSetting(givenRepository);

        JpaRepository<?, ?> actual = checker.getProperty(givenSetting);
        assertSame(givenRepository, actual);
    }

    private static final class TestReplicationSetting extends ReplicationSetting<Object, Object> {
        private static final String GIVEN_TOPIC = "test-topic";

        public TestReplicationSetting(JpaRepository<Object, Object> repository) {
            super(GIVEN_TOPIC, repository);
        }
    }
}
