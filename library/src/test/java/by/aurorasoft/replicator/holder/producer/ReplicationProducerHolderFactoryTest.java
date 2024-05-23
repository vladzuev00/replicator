package by.aurorasoft.replicator.holder.producer;

import by.aurorasoft.replicator.holder.service.ReplicatedServiceHolder;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.producer.ReplicationProducerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationProducerHolderFactoryTest {
    private static final String FIELD_NAME_PRODUCERS_BY_SERVICES = "producersByServices";

    @Mock
    private ReplicatedServiceHolder mockedServiceHolder;

    @Mock
    private ReplicationProducerFactory mockedProducerFactory;

    private ReplicationProducerHolderFactory holderFactory;

    @Before
    public void initializeHolderFactory() {
        holderFactory = new ReplicationProducerHolderFactory(mockedServiceHolder, mockedProducerFactory);
    }

    @Test
    public void holderShouldBeCreated() {
        final Object firstGivenService = new Object();
        final Object secondGivenService = new Object();

        putIntoHolder(firstGivenService, secondGivenService);

        final ReplicationProducer firstGivenProducer = mockProducerFor(firstGivenService);
        final ReplicationProducer secondGivenProducer = mockProducerFor(secondGivenService);

        final ReplicationProducerHolder actual = holderFactory.create();
        final var actualProducersByServices = getProducersByServices(actual);
        final var expectedProducersByServices = Map.of(
                firstGivenService, firstGivenProducer,
                secondGivenService, secondGivenProducer
        );
        assertEquals(expectedProducersByServices, actualProducersByServices);
    }

    private void putIntoHolder(final Object... services) {
        final List<Object> givenServices = asList(services);
        when(mockedServiceHolder.getServices()).thenReturn(givenServices);
    }

    private ReplicationProducer mockProducerFor(final Object service) {
        final ReplicationProducer producer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(same(service))).thenReturn(producer);
        return producer;
    }

    @SuppressWarnings("unchecked")
    private Map<Object, ReplicationProducer> getProducersByServices(final ReplicationProducerHolder holder) {
        return getFieldValue(holder, FIELD_NAME_PRODUCERS_BY_SERVICES, Map.class);
    }
}
