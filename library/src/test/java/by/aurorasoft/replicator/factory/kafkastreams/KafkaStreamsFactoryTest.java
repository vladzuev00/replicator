package by.aurorasoft.replicator.factory.kafkastreams;

import by.aurorasoft.replicator.util.ShutdownHookUtil;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.util.List;

import static by.aurorasoft.replicator.util.ShutdownHookUtil.addShutdownHook;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

public final class KafkaStreamsFactoryTest {
    private final KafkaStreamsFactory factory = new KafkaStreamsFactory();

    @Test
    public void streamsShouldBeCreated() {
        try (MockedConstruction<KafkaStreams> mockedConstruction = mockConstruction(KafkaStreams.class);
             MockedStatic<ShutdownHookUtil> mockedShutdownHookUtil = mockStatic(ShutdownHookUtil.class)) {
            Topology givenTopology = mock(Topology.class);
            StreamsConfig givenConfig = mock(StreamsConfig.class);

            KafkaStreams actual = factory.create(givenTopology, givenConfig);

            List<KafkaStreams> constructedStreams = mockedConstruction.constructed();
            assertEquals(1, constructedStreams.size());
            KafkaStreams constructedStream = constructedStreams.get(0);

            assertSame(constructedStream, actual);

            verifySuccessPlanningClosure(actual, mockedShutdownHookUtil);
        }
    }

    @Test
    public void streamsShouldNotBeCreatedBecauseOfFailedAddingShutdownHook() {
        try (MockedConstruction<KafkaStreams> mockedConstruction = mockConstruction(KafkaStreams.class);
             MockedStatic<ShutdownHookUtil> mockedShutdownHookUtil = mockStatic(ShutdownHookUtil.class)) {
            Topology givenTopology = mock(Topology.class);
            StreamsConfig givenConfig = mock(StreamsConfig.class);

            mockFailedAddingShutdownHook(mockedShutdownHookUtil);

            createStreamsVerifyingException(givenTopology, givenConfig);

            List<KafkaStreams> constructedStreams = mockedConstruction.constructed();
            assertEquals(1, constructedStreams.size());
            KafkaStreams constructedStream = constructedStreams.get(0);

            verifyFailedPlanningClosure(constructedStream, mockedShutdownHookUtil);
        }
    }

    private void verifySuccessPlanningClosure(KafkaStreams streams, MockedStatic<ShutdownHookUtil> hookUtil) {
        verifyPlanningClosure(streams, hookUtil, 1);
    }

    private void verifyFailedPlanningClosure(KafkaStreams streams, MockedStatic<ShutdownHookUtil> hookUtil) {
        verifyPlanningClosure(streams, hookUtil, 2);
    }

    private void verifyPlanningClosure(KafkaStreams streams,
                                       MockedStatic<ShutdownHookUtil> hookUtil,
                                       int closureTimes) {
        captureClosingTask(hookUtil).run();
        verify(streams, times(closureTimes)).close();
    }

    private Runnable captureClosingTask(MockedStatic<ShutdownHookUtil> hookUtil) {
        ArgumentCaptor<Runnable> taskCaptor = forClass(Runnable.class);
        hookUtil.verify(() -> addShutdownHook(taskCaptor.capture()), times(1));
        return taskCaptor.getValue();
    }

    private void mockFailedAddingShutdownHook(MockedStatic<ShutdownHookUtil> hookUtil) {
        hookUtil.when(() -> addShutdownHook(any(Runnable.class))).thenThrow(TestException.class);
    }

    private void createStreamsVerifyingException(Topology topology, StreamsConfig config) {
        boolean exceptionArisen;
        try {
            factory.create(topology, config);
            exceptionArisen = false;
        } catch (TestException cause) {
            exceptionArisen = true;
        }
        assertTrue(exceptionArisen);
    }

    private static final class TestException extends RuntimeException {

    }
}
