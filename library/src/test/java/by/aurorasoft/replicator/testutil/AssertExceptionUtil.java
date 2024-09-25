package by.aurorasoft.replicator.testutil;

import lombok.experimental.UtilityClass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.util.StringUtils.trimAllWhitespace;

@UtilityClass
public final class AssertExceptionUtil {

    public static void assertException(Runnable task, Class<? extends Throwable> expectedType, String expectedMessage) {
        boolean exceptionArisen = false;
        try {
            task.run();
        } catch (Throwable actual) {
            exceptionArisen = true;
            assertTrue(expectedType.isInstance(actual));
            assertEquals(trimAllWhitespace(expectedMessage), trimAllWhitespace(actual.getMessage()));
        }
        assertTrue(exceptionArisen);
    }
}
