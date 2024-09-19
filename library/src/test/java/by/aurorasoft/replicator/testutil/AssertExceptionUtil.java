package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplicationTest;
import by.aurorasoft.replicator.testcrud.TestEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.util.StringUtils.trimAllWhitespace;

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
            actual.printStackTrace();
            exceptionArisen = true;
            assertTrue(expectedExceptionType.isInstance(actual));
            assertEquals(trimAllWhitespace(expectedMessage), trimAllWhitespace(actual.getMessage()));
        }
        assertTrue(exceptionArisen);
    }

    public void executeExpectingException(Runnable task, Class<? extends Throwable> expectedExceptionType) {
        boolean exceptionArisen = false;
        try {
            task.run();
        } catch (Throwable actual) {
            actual.printStackTrace();
            exceptionArisen = true;
            assertTrue(expectedExceptionType.isInstance(actual));
        }
        assertTrue(exceptionArisen);
    }
}
