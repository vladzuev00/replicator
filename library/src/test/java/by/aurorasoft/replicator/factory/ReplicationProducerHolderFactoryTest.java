package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.holder.ReplicatedServiceHolder;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationProducerHolderFactoryTest {
    private static final String FIELD_NAME_HOLDER_PRODUCERS_BY_SERVICES = "producersByServices";

    @Mock
    private ReplicatedServiceHolder mockedServiceHolder;

    @Mock
    private ReplicationProducerFactory mockedProducerFactory;

    private ReplicationProducerHolderFactory factory;

    @Before
    public void initializeFactory() {
        factory = new ReplicationProducerHolderFactory(mockedServiceHolder, mockedProducerFactory);
    }

    @Test
    public void holderShouldBeCreated() {
        final AbsServiceRUD<?, ?, ?, ?, ?> firstGivenService = mock(AbsServiceRUD.class);
        final AbsServiceRUD<?, ?, ?, ?, ?> secondGivenService = mock(AbsServiceRUD.class);

        bindServicesToHolder(firstGivenService, secondGivenService);

        final ReplicationProducer<?> firstGivenProducer = mock(ReplicationProducer.class);
        bindProducerToService(firstGivenProducer, firstGivenService);

        final ReplicationProducer<?> secondGivenProducer = mock(ReplicationProducer.class);
        bindProducerToService(secondGivenProducer, secondGivenService);

        final ReplicationProducerHolder actual = factory.create();
        final var actualProducersByServices = findProducersByServices(actual);
        final var expectedProducersByServices = Map.of(
                firstGivenService, firstGivenProducer,
                secondGivenService, secondGivenProducer
        );
        assertEquals(expectedProducersByServices, actualProducersByServices);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void bindServicesToHolder(final AbsServiceRUD<?, ?, ?, ?, ?>... services) {
        final List givenServices = asList(services);
        when(mockedServiceHolder.getServices()).thenReturn(givenServices);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void bindProducerToService(final ReplicationProducer producer, final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        when(mockedProducerFactory.create(same(service))).thenReturn(producer);
    }

    @SuppressWarnings("unchecked")
    private static Map<AbsServiceRUD<?, ?, ?, ?, ?>, ReplicationProducer<?>> findProducersByServices(
            final ReplicationProducerHolder holder
    ) {
        return getFieldValue(holder, FIELD_NAME_HOLDER_PRODUCERS_BY_SERVICES, Map.class);
    }
}
