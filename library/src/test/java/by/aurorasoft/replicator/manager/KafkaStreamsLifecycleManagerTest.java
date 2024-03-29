package by.aurorasoft.replicator.manager;

import org.apache.kafka.streams.KafkaStreams;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaStreamsLifecycleManagerTest {
    private static final String FIELD_NAME_ELEMENTS = "elements";

    @Mock
    private KafkaStreams firstMockedStreams;

    @Mock
    private KafkaStreams secondMockedStreams;

    private KafkaStreamsLifecycleManager manager;

    @Before
    public void initializeManagerAndRegisterGivenStreams() {
        manager = new KafkaStreamsLifecycleManager();
        manager.register(firstMockedStreams);
        manager.register(secondMockedStreams);
    }

    @Test
    public void elementsShouldBeRegistered() {
        final List<KafkaStreams> actual = getManagerElements(manager);
        final List<KafkaStreams> expected = List.of(firstMockedStreams, secondMockedStreams);
        assertEquals(expected, actual);
    }

    @Test
    public void elementsShouldBeClosed() {
        manager.closeAll();

        verify(firstMockedStreams, times(1)).close();
        verify(secondMockedStreams, times(1)).close();
    }

    @SuppressWarnings("unchecked")
    private static List<KafkaStreams> getManagerElements(final KafkaStreamsLifecycleManager manager) {
        return getFieldValue(manager, FIELD_NAME_ELEMENTS, List.class);
    }
}
