package by.aurorasoft.replicator.consuming.starter.factory;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

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
        try (final MockedConstruction<KafkaStreams> mockedConstruction = mockConstruction(KafkaStreams.class)) {
            final Topology givenTopology = mock(Topology.class);
            final StreamsConfig givenConfig = mock(StreamsConfig.class);

            final KafkaStreams actual = factory.create(givenTopology, givenConfig);
            verifyConstruction(mockedConstruction);
            final KafkaStreams expected = getConstructedObject(mockedConstruction);
            assertSame(expected, actual);

            verifyNotClosed(actual);
            verifyClosingShutdownHook(actual);
        }
    }

    @Test
    public void streamsShouldNotBeCreatedBecauseOfFailedAddingShutdownHook() {
        try (final MockedConstruction<KafkaStreams> mockedConstruction = mockConstruction(KafkaStreams.class)) {
            final Topology givenTopology = mock(Topology.class);
            final StreamsConfig givenConfig = mock(StreamsConfig.class);

            throwExceptionOnAddingShutdownHook();

            createStreamsVerifyingException(givenTopology, givenConfig);

            verifyConstruction(mockedConstruction);
            final KafkaStreams constructedStreams = getConstructedObject(mockedConstruction);

            verifyClosed(constructedStreams);
            verifyAddShutdownHook();
        }
    }

    private void verifyConstruction(final MockedConstruction<?> mockedConstruction) {
        assertEquals(1, mockedConstruction.constructed().size());
    }

    private <T> T getConstructedObject(final MockedConstruction<T> mockedConstruction) {
        return mockedConstruction.constructed().get(0);
    }

    private void verifyNotClosed(final KafkaStreams streams) {
        verifyCallingClose(streams, 0);
    }

    private void verifyClosed(final KafkaStreams streams) {
        verifyCallingClose(streams, 1);
    }

    private void verifyCallingClose(final KafkaStreams streams, final int times) {
        verify(streams, times(times)).close();
    }

    private void verifyClosingShutdownHook(final KafkaStreams streams) {
        verifyAddShutdownHook();
        final Runnable capturedThread = threadArgumentCaptor.getValue();
        capturedThread.run();
        verifyClosed(streams);
    }

    private void throwExceptionOnAddingShutdownHook() {
        doThrow(TestException.class)
                .when(mockedRuntime)
                .addShutdownHook(any(Thread.class));
    }

    private void createStreamsVerifyingException(final Topology topology, final StreamsConfig config) {
        boolean exceptionArisen;
        try {
            factory.create(topology, config);
            exceptionArisen = false;
        } catch (final TestException cause) {
            exceptionArisen = true;
        }
        assertTrue(exceptionArisen);
    }

    private void verifyAddShutdownHook() {
        verify(mockedRuntime, times(1)).addShutdownHook(threadArgumentCaptor.capture());
    }

    private static final class TestException extends RuntimeException {

        @SuppressWarnings("unused")
        public TestException() {

        }

        @SuppressWarnings("unused")
        public TestException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public TestException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public TestException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
