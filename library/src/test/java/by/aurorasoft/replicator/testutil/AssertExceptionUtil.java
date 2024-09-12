package by.aurorasoft.replicator.testutil;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//TODO: использовать везде где есть exceptionArisen переменная
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
            assertEquals(expectedMessage, actual.getMessage());
        }
        assertTrue(exceptionArisen);
    }
}
