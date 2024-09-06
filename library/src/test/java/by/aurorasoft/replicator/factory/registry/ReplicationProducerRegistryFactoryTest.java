package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.testcrud.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerRegistryFactoryTest {
    private static final String FIELD_NAME_REGISTRY_PRODUCERS_BY_SERVICES = "producersByServices";

    @Mock
    private ApplicationContext mockedApplicationContext;

    @Mock
    private ReplicationProducerFactory mockedProducerFactory;

    private ReplicationProducerRegistryFactory factory;

    @BeforeEach
    public void initializeFactory() {
        factory = new ReplicationProducerRegistryFactory(mockedApplicationContext, mockedProducerFactory);
    }

    @Test
    public void registryShouldBeCreated() {
        TestService firstGivenService = new TestService(null);
        TestService secondGivenService = new TestService(null);

        Map<String, Object> givenServicesByNames = Map.of(
                "firstService", firstGivenService,
                "secondService", secondGivenService
        );
        when(mockedApplicationContext.getBeansWithAnnotation(same(ReplicatedService.class)))
                .thenReturn(givenServicesByNames);

        ReplicationProducer firstGivenProducer = mockProducerFor(firstGivenService);
        ReplicationProducer secondGivenProducer = mockProducerFor(secondGivenService);

        ReplicationProducerRegistry actual = factory.create();
        Map<Object, ReplicationProducer> actualProducersByServices = getProducersByServices(actual);
        Map<Object, ReplicationProducer> expectedProducersByServices = Map.of(
                firstGivenService, firstGivenProducer,
                secondGivenService, secondGivenProducer
        );
        assertEquals(expectedProducersByServices, actualProducersByServices);
    }

    private ReplicationProducer mockProducerFor(TestService service) {
        ReplicationProducer producer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(same(service))).thenReturn(producer);
        return producer;
    }

    @SuppressWarnings("unchecked")
    private Map<Object, ReplicationProducer> getProducersByServices(ReplicationProducerRegistry registry) {
        return getFieldValue(registry, FIELD_NAME_REGISTRY_PRODUCERS_BY_SERVICES, Map.class);
    }
}
