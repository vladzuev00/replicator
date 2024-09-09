package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import org.junit.jupiter.api.Test;

import static by.aurorasoft.replicator.testutil.ReplicatedServiceUtil.createReplicatedService;
import static by.aurorasoft.replicator.testutil.TopicConfigUtil.createTopicConfig;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ReplicatedServiceUniqueTopicValidatorTest {
    private final ReplicatedServiceUniqueTopicValidator validator = new ReplicatedServiceUniqueTopicValidator();

    @Test
    public void propertyShouldBeGot() {
        String givenTopicName = "test-topic";
        ReplicatedService givenService = createReplicatedService(createTopicConfig(givenTopicName));

        String actual = validator.getProperty(givenService);
        assertSame(givenTopicName, actual);
    }
}
