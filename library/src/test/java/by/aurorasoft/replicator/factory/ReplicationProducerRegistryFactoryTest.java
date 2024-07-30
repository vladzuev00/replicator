package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicatedServiceRegistry;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.v1.service.FirstTestV1CRUDService;
import by.aurorasoft.replicator.v2.service.SecondTestV2CRUDService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationProducerRegistryFactoryTest {
    private static final String FIELD_NAME_PRODUCERS_BY_SERVICES = "producersByServices";

    @Mock
    private ReplicatedServiceRegistry mockedServiceRegistry;

    @Mock
    private ReplicationProducerFactory mockedProducerFactory;

    private ReplicationProducerRegistryFactory registryFactory;

    @Captor
    private ArgumentCaptor<ReplicatedService> replicatedServiceArgumentCaptor;

    @Before
    public void initializeRegistryFactory() {
        registryFactory = new ReplicationProducerRegistryFactory(mockedServiceRegistry, mockedProducerFactory);
    }

    @Test
    public void registryShouldBeCreated() {
        Object firstGivenService = new FirstTestV1CRUDService();
        Object secondGivenService = new SecondTestV2CRUDService();
        Set<Object> givenServices = new LinkedHashSet<>(List.of(firstGivenService, secondGivenService));
        when(mockedServiceRegistry.getServices()).thenReturn(givenServices);

        ReplicationProducer firstGivenProducer = mock(ReplicationProducer.class);
        ReplicationProducer secondGivenProducer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(any(ReplicatedService.class)))
                .thenReturn(firstGivenProducer)
                .thenReturn(secondGivenProducer);

        ReplicationProducerRegistry actual = registryFactory.create();
        var actualProducersByServices = getProducersByServices(actual);
        var expectedProducersByServices = Map.of(
                firstGivenService, firstGivenProducer,
                secondGivenService, secondGivenProducer
        );
        assertEquals(expectedProducersByServices, actualProducersByServices);

        //TODO: verifyReplicatedServices
    }

    @SuppressWarnings("unchecked")
    private Map<Object, ReplicationProducer> getProducersByServices(ReplicationProducerRegistry registry) {
        return getFieldValue(registry, FIELD_NAME_PRODUCERS_BY_SERVICES, Map.class);
    }

    private void verifyReplicatedServices(List<ReplicatedService> expected) {
        verify(mockedProducerFactory, times(expected.size())).create(replicatedServiceArgumentCaptor.capture());
        List<ReplicatedService> actual = replicatedServiceArgumentCaptor.getAllValues();
        throw new RuntimeException();
//        range(0, expected.size()).forEach(i -> );
    }
}
