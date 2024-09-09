package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.testcrud.TestService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReplicatedServiceUniqueTopicValidatorTest {
    private final ReplicatedServiceUniqueTopicValidator validator = new ReplicatedServiceUniqueTopicValidator();

    @Test
    public void propertyShouldBeGot() {
        TestService givenService = new TestService(null);

        String actual = validator.getProperty(givenService);
        String expected = "sync-dto";
        assertEquals(expected, actual);
    }
}
