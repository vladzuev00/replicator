package by.aurorasoft.replicator.holder.producer;

import by.aurorasoft.replicator.holder.service.ReplicatedServiceRegistry;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.producer.ReplicationProducerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.Set;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationProducerHolderFactoryTest {
    private static final String FIELD_NAME_PRODUCERS_BY_SERVICES = "producersByServices";

    @Mock
    private ReplicatedServiceRegistry mockedServiceHolder;

    @Mock
    private ReplicationProducerFactory mockedProducerFactory;

    private ReplicationProducerRegistryFactory holderFactory;

    @Before
    public void initializeHolderFactory() {
        holderFactory = new ReplicationProducerRegistryFactory(mockedServiceHolder, mockedProducerFactory);
    }

    @Test
    public void holderShouldBeCreated() {
        final Object firstGivenService = new Object();
        final Object secondGivenService = new Object();
        final Set<Object> givenServices = Set.of(firstGivenService, secondGivenService);
        when(mockedServiceHolder.getServices()).thenReturn(givenServices);

        final ReplicationProducer firstGivenProducer = mockProducerFor(firstGivenService);
        final ReplicationProducer secondGivenProducer = mockProducerFor(secondGivenService);

        final ReplicationProducerRegistry actual = holderFactory.create();
        final var actualProducersByServices = getProducersByServices(actual);
        final var expectedProducersByServices = Map.of(
                firstGivenService, firstGivenProducer,
                secondGivenService, secondGivenProducer
        );
        assertEquals(expectedProducersByServices, actualProducersByServices);
    }

    private ReplicationProducer mockProducerFor(final Object service) {
        final ReplicationProducer producer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(same(service))).thenReturn(producer);
        return producer;
    }

    @SuppressWarnings("unchecked")
    private Map<Object, ReplicationProducer> getProducersByServices(final ReplicationProducerRegistry holder) {
        return getFieldValue(holder, FIELD_NAME_PRODUCERS_BY_SERVICES, Map.class);
    }
}
