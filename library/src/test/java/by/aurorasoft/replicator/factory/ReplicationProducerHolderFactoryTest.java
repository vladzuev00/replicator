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
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void holderShouldBeCreated() {
        final AbsServiceRUD<?, ?, ?, ?, ?> firstGivenService = mock(AbsServiceRUD.class);
        final AbsServiceRUD<?, ?, ?, ?, ?> secondGivenService = mock(AbsServiceRUD.class);

        final List givenServices = List.of(firstGivenService, secondGivenService);
        when(mockedServiceHolder.getServices()).thenReturn(givenServices);

        final ReplicationProducer firstGivenProducer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(same(firstGivenService))).thenReturn(firstGivenProducer);

        final ReplicationProducer secondGivenProducer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(same(secondGivenService))).thenReturn(secondGivenProducer);

        final ReplicationProducerHolder actual = factory.create();
        final var actualProducersByServices = findProducersByServices(actual);
        final var expectedProducersByServices = Map.of(
                firstGivenService, firstGivenProducer,
                secondGivenService, secondGivenProducer
        );
        assertEquals(expectedProducersByServices, actualProducersByServices);
    }

    @SuppressWarnings("unchecked")
    private static Map<AbsServiceRUD<?, ?, ?, ?, ?>, ReplicationProducer<?>> findProducersByServices(
            final ReplicationProducerHolder holder
    ) {
        return getFieldValue(holder, FIELD_NAME_HOLDER_PRODUCERS_BY_SERVICES, Map.class);
    }
}
