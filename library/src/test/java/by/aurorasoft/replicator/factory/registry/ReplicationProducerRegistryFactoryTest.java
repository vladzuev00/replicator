package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.loader.ReplicatedServiceLoader;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.starter.ReplicationProducingStarter;
import by.aurorasoft.replicator.testcrud.TestService;
import by.aurorasoft.replicator.validator.ReplicatedServiceUniqueTopicValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerRegistryFactoryTest {
    private static final String FIELD_NAME_REGISTRY_PRODUCERS_BY_SERVICES = "producersByServices";

    @Mock
    private ReplicatedServiceUniqueTopicValidator mockedUniqueTopicValidator;

    @Mock
    private ReplicatedServiceLoader mockedServiceLoader;

    @Mock
    private ReplicationProducingStarter mockedProducingStarter;

    private ReplicationProducerRegistryFactory factory;

    @BeforeEach
    public void initializeFactory() {
        factory = new ReplicationProducerRegistryFactory(
                mockedUniqueTopicValidator,
                mockedServiceLoader,
                mockedProducingStarter
        );
    }

    @Test
    public void registryShouldBeCreated() {
        TestService firstGivenService = new TestService(null);
        TestService secondGivenService = new TestService(null);

        Collection<Object> givenServices = List.of(firstGivenService, secondGivenService);
        when(mockedServiceLoader.load()).thenReturn(givenServices);

        ReplicationProducer firstGivenProducer = mockStartProducingFor(firstGivenService);
        ReplicationProducer secondGivenProducer = mockStartProducingFor(secondGivenService);

        ReplicationProducerRegistry actual = factory.create();
        Map<Object, ReplicationProducer> actualProducersByServices = getProducersByServices(actual);
        Map<Object, ReplicationProducer> expectedProducersByServices = Map.of(
                firstGivenService, firstGivenProducer,
                secondGivenService, secondGivenProducer
        );
        assertEquals(expectedProducersByServices, actualProducersByServices);

        verify(mockedUniqueTopicValidator, times(1)).validate(same(givenServices));
    }

    private ReplicationProducer mockStartProducingFor(TestService service) {
        ReplicationProducer producer = mock(ReplicationProducer.class);
        when(mockedProducingStarter.startReturningProducer(same(service))).thenReturn(producer);
        return producer;
    }

    @SuppressWarnings("unchecked")
    private Map<Object, ReplicationProducer> getProducersByServices(ReplicationProducerRegistry registry) {
        return getFieldValue(registry, FIELD_NAME_REGISTRY_PRODUCERS_BY_SERVICES, Map.class);
    }
}
