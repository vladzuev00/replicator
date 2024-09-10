package by.aurorasoft.replicator.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static by.aurorasoft.replicator.util.ShutdownHookUtil.addShutdownHook;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ShutdownHookUtilTest {

    @Captor
    private ArgumentCaptor<Thread> threadCaptor;

    @Test
    public void shutdownHookUtilShouldBeAdded()
            throws InterruptedException {
        try (MockedStatic<Runtime> mockedStaticRuntime = mockStatic(Runtime.class)) {
            TestTask givenTask = new TestTask();

            Runtime givenRuntime = mock(Runtime.class);
            //noinspection ResultOfMethodCallIgnored
            mockedStaticRuntime.when(Runtime::getRuntime).thenReturn(givenRuntime);

            addShutdownHook(givenTask);

            verify(givenRuntime, times(1)).addShutdownHook(threadCaptor.capture());
            Thread capturedThread = threadCaptor.getValue();
            capturedThread.start();
            capturedThread.join();
            assertTrue(givenTask.executed);
        }
    }

    private static final class TestTask implements Runnable {
        private volatile boolean executed;

        @Override
        public void run() {
            executed = true;
        }
    }
}
