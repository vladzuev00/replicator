package by.aurorasoft.replicator.testutil;

import lombok.experimental.UtilityClass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.util.StringUtils.trimAllWhitespace;

@UtilityClass
public final class AssertExceptionUtil {

    public static void executeExpectingException(Runnable task,
                                                 Class<? extends Throwable> expectedExceptionType,
                                                 String expectedMessage) {
        boolean exceptionArisen = false;
        try {
            task.run();
        } catch (Throwable actual) {
            exceptionArisen = true;
            assertTrue(expectedExceptionType.isInstance(actual));
            assertEquals(trimAllWhitespace(expectedMessage), trimAllWhitespace(actual.getMessage()));
        }
        assertTrue(exceptionArisen);
    }
}
