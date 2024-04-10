package by.aurorasoft.replicator.factory;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.MockedConstruction.Context;
import org.mockito.junit.MockitoJUnitRunner;

import static org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaStreamsFactoryTest {
    private final KafkaStreamsFactory factory = new KafkaStreamsFactory();

    private MockedStatic<Runtime> mockedStaticRuntime;

    @Mock
    private Runtime mockedRuntime;

    @Captor
    private ArgumentCaptor<StreamsUncaughtExceptionHandler> exceptionHandlerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Thread> threadArgumentCaptor;

    @Before
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void bindMockedRuntime() {
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
            verifyStreamsConstruction(mockedConstruction);
            final KafkaStreams expected = getConstructedStreams(mockedConstruction);
            assertSame(expected, actual);

            verifySettingExceptionHandler(actual);
            verifyNotClosed(actual);
            verifyClosingShutdownHook(actual);
        }
    }

    @Test
    public void streamsShouldNotBeCreatedBecauseOfFailedSettingExceptionHandler() {
        try (final MockedConstruction<KafkaStreams> mockedConstruction = mockConstruction(KafkaStreams.class, KafkaStreamsFactoryTest::throwExceptionWhenSettingHandler)) {
            final Topology givenTopology = mock(Topology.class);
            final StreamsConfig givenConfig = mock(StreamsConfig.class);

            createStreamsVerifyingException(givenTopology, givenConfig);

            verifyStreamsConstruction(mockedConstruction);
            verifySettingExceptionHandler(mockedConstruction.constructed().get(0));
            verifyCloseConstructedStreams(mockedConstruction);
            verifyNoShutdownHook();
        }
    }

    private void verifyStreamsConstruction(final MockedConstruction<KafkaStreams> mockedConstruction) {
        assertEquals(1, mockedConstruction.constructed().size());
    }

    private KafkaStreams getConstructedStreams(final MockedConstruction<KafkaStreams> mockedConstruction) {
        return mockedConstruction.constructed().get(0);
    }

    private void verifySettingExceptionHandler(final KafkaStreams streams) {
        verify(streams, times(1)).setUncaughtExceptionHandler(exceptionHandlerArgumentCaptor.capture());
        final StreamsUncaughtExceptionHandler capturedHandler = exceptionHandlerArgumentCaptor.getValue();
        final StreamThreadExceptionResponse actualResponse = capturedHandler.handle(null);
        assertSame(SHUTDOWN_APPLICATION, actualResponse);
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
        verify(mockedRuntime, times(1)).addShutdownHook(threadArgumentCaptor.capture());
        final Runnable capturedThread = threadArgumentCaptor.getValue();
        capturedThread.run();
        verifyClosed(streams);
    }


    private void verifyNoShutdownHook() {
        verify(mockedRuntime, times(0)).addShutdownHook(any(Thread.class));
    }

    private static void throwExceptionWhenSettingHandler(final KafkaStreams streams, final Context ignored) {
        doThrow(TestException.class)
                .when(streams)
                .setUncaughtExceptionHandler(any(StreamsUncaughtExceptionHandler.class));
    }

    private void createStreamsVerifyingException(final Topology topology, final StreamsConfig config) {
//        boolean exceptionArisen;
//        try {
//            createStreamsVerifyingConstruction(topology, config);
//            exceptionArisen = false;
//        } catch (final TestException cause) {
//            exceptionArisen = true;
//        }
//        assertTrue(exceptionArisen);
    }

    private void verifyCloseConstructedStreams(final MockedConstruction<KafkaStreams> mockedConstruction) {
        verify(getConstructedStreams(mockedConstruction), times(1)).close();
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
