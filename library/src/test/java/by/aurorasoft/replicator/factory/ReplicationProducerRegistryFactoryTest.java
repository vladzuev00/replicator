//package by.aurorasoft.replicator.factory;
//
//import by.aurorasoft.replicator.producer.ReplicationProducer;
//import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
//import by.aurorasoft.replicator.registry.ReplicatedServiceRegistry;
//import by.aurorasoft.replicator.v1.service.FirstTestV1CRUDService;
//import by.aurorasoft.replicator.v2.service.SecondTestV2CRUDService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ReplicationProducerRegistryFactoryTest {
//    private static final String FIELD_NAME_PRODUCERS_BY_SERVICES = "producersByServices";
//
//    @Mock
//    private ReplicatedServiceRegistry mockedServiceRegistry;
//
//    @Mock
//    private ReplicationProducerFactory mockedProducerFactory;
//
//    private ReplicationProducerRegistryFactory registryFactory;
//
//    @Before
//    public void initializeRegistryFactory() {
//        registryFactory = new ReplicationProducerRegistryFactory(mockedServiceRegistry, mockedProducerFactory);
//    }
//
//    @Test
//    public void registryShouldBeCreated() {
//        Object firstGivenService = new FirstTestV1CRUDService();
//        Object secondGivenService = new SecondTestV2CRUDService();
//        Set<Object> givenServices = new LinkedHashSet<>(List.of(firstGivenService, secondGivenService));
//        when(mockedServiceRegistry.getServices()).thenReturn(givenServices);
//
//        ReplicationProducer firstGivenProducer = mockProducerFor(firstGivenService);
//        ReplicationProducer secondGivenProducer = mockProducerFor(secondGivenService);
//
//        ReplicationProducerRegistry actual = registryFactory.create();
//        var actualProducersByServices = getProducersByServices(actual);
//        var expectedProducersByServices = Map.of(
//                firstGivenService, firstGivenProducer,
//                secondGivenService, secondGivenProducer
//        );
//        assertEquals(expectedProducersByServices, actualProducersByServices);
//    }
//
//    private ReplicationProducer mockProducerFor(Object service) {
//        ReplicationProducer producer = mock(ReplicationProducer.class);
//        when(mockedProducerFactory.create(same(service))).thenReturn(producer);
//        return producer;
//    }
//
//    @SuppressWarnings("unchecked")
//    private Map<Object, ReplicationProducer> getProducersByServices(ReplicationProducerRegistry registry) {
//        return getFieldValue(registry, FIELD_NAME_PRODUCERS_BY_SERVICES, Map.class);
//    }
//}
