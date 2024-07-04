package by.aurorasoft.replicator.factory;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaStreamsFactoryTest {
    private final KafkaStreamsFactory factory = new KafkaStreamsFactory();

    private MockedStatic<Runtime> mockedStaticRuntime;

    @Mock
    private Runtime mockedRuntime;

    @Captor
    private ArgumentCaptor<Thread> threadArgumentCaptor;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void mockRuntime() {
        mockedStaticRuntime = mockStatic(Runtime.class);
        mockedStaticRuntime.when(Runtime::getRuntime).thenReturn(mockedRuntime);
    }

    @After
    public void closeMockedStaticRuntime() {
        mockedStaticRuntime.close();
    }

    @Test
    public void streamsShouldBeCreated() {
        try (MockedConstruction<KafkaStreams> mockedConstruction = mockConstruction(KafkaStreams.class)) {
            Topology givenTopology = mock(Topology.class);
            StreamsConfig givenConfig = mock(StreamsConfig.class);

            KafkaStreams actual = factory.create(givenTopology, givenConfig);

            List<KafkaStreams> constructedStreams = mockedConstruction.constructed();
            assertEquals(1, constructedStreams.size());
            KafkaStreams constructedStream = constructedStreams.get(0);

            assertSame(constructedStream, actual);

            verifyNotClosed(actual);
            verifyClosingShutdownHook(actual);
        }
    }

    @Test
    public void streamsShouldNotBeCreatedBecauseOfFailedAddingShutdownHook() {
        try (MockedConstruction<KafkaStreams> mockedConstruction = mockConstruction(KafkaStreams.class)) {
            Topology givenTopology = mock(Topology.class);
            StreamsConfig givenConfig = mock(StreamsConfig.class);

            mockExceptionOnAddingShutdownHook();

            createStreamsVerifyingException(givenTopology, givenConfig);

            List<KafkaStreams> constructedStreams = mockedConstruction.constructed();
            assertEquals(1, constructedStreams.size());
            KafkaStreams constructedStream = constructedStreams.get(0);

            verifyClosed(constructedStream);
            verifyAddShutdownHook();
        }
    }

    private void verifyNotClosed(KafkaStreams streams) {
        verifyCallingClose(streams, 0);
    }

    private void verifyClosed(KafkaStreams streams) {
        verifyCallingClose(streams, 1);
    }

    private void verifyCallingClose(KafkaStreams streams, int times) {
        verify(streams, times(times)).close();
    }

    private void verifyClosingShutdownHook(KafkaStreams streams) {
        verifyAddShutdownHook();
        Runnable capturedThread = threadArgumentCaptor.getValue();
        capturedThread.run();
        verifyClosed(streams);
    }

    private void verifyAddShutdownHook() {
        verify(mockedRuntime, times(1)).addShutdownHook(threadArgumentCaptor.capture());
    }

    private void mockExceptionOnAddingShutdownHook() {
        doThrow(TestException.class)
                .when(mockedRuntime)
                .addShutdownHook(any(Thread.class));
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
