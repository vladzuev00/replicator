//package by.aurorasoft.replicator.validator;
//
//import by.aurorasoft.replicator.model.setting.ReplicationSetting;
//import by.aurorasoft.replicator.testcrud.TestEntity;
//import by.aurorasoft.replicator.testcrud.TestRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import static org.junit.jupiter.api.Assertions.assertSame;
//
//public final class ReplicationUniqueTopicCheckerTest {
//    private final ReplicationUniqueTopicChecker checker = new ReplicationUniqueTopicChecker();
//
//    @Test
//    public void propertyShouldBeGot() {
//        String givenTopic = "test-topic";
//        TestReplicationSetting givenSetting = new TestReplicationSetting(givenTopic);
//
//        String actual = checker.getProperty(givenSetting);
//        assertSame(givenTopic, actual);
//    }
//
//    private static final class TestReplicationSetting extends ReplicationSetting<TestEntity, Long> {
//        private static final JpaRepository<TestEntity, Long> GIVEN_REPOSITORY = new TestRepository();
//
//        public TestReplicationSetting(String topic) {
//            super(topic, GIVEN_REPOSITORY);
//        }
//    }
//}
