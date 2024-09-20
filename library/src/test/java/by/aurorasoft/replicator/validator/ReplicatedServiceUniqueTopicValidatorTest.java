package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.testcrud.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReplicatedServiceUniqueTopicValidatorTest extends AbstractSpringBootTest {

    @Autowired
    private ReplicatedServiceUniqueTopicValidator validator;

    @Autowired
    private TestService service;

    @Test
    public void propertyShouldBeGot() {
        String actual = validator.getProperty(service);
        String expected = "sync-dto";
        assertEquals(expected, actual);
    }
}
