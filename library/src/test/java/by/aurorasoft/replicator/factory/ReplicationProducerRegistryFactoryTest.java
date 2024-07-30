package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ViewConfig;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicatedServiceRegistry;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.v1.service.FirstTestV1CRUDService;
import by.aurorasoft.replicator.v2.dto.TestV2Dto;
import by.aurorasoft.replicator.v2.service.SecondTestV2CRUDService;
import org.apache.kafka.common.serialization.LongSerializer;
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

import static by.aurorasoft.replicator.testutil.ProducerConfigUtil.createProducerConfig;
import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static by.aurorasoft.replicator.testutil.ReplicatedServiceUtil.checkEquals;
import static by.aurorasoft.replicator.testutil.ReplicatedServiceUtil.createReplicatedService;
import static by.aurorasoft.replicator.testutil.TopicConfigUtil.createTopicConfig;
import static by.aurorasoft.replicator.testutil.ViewConfigUtil.createViewConfig;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
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
        Map<Object, ReplicationProducer> actualProducersByServices = getProducersByServices(actual);
        Map<Object, ReplicationProducer> expectedProducersByServices = Map.of(
                firstGivenService, firstGivenProducer,
                secondGivenService, secondGivenProducer
        );
        assertEquals(expectedProducersByServices, actualProducersByServices);

        List<ReplicatedService> expectedReplicatedServices = List.of(
                createReplicatedService(
                        createProducerConfig(LongSerializer.class, 10, 500, 100000),
                        createTopicConfig("first-topic", 1, 1),
                        new ViewConfig[]{}
                ),
                createReplicatedService(
                        createProducerConfig(LongSerializer.class, 15, 515, 110000),
                        createTopicConfig("second-topic", 2, 2),
                        new ViewConfig[]{
                                createViewConfig(
                                        TestV2Dto.class,
                                        new String[]{"first-field"},
                                        new String[]{"second-field"}
                                ),
                                createViewConfig(
                                        TestV2Dto.class,
                                        new String[]{"third-field", "fourth-field"},
                                        new String[]{"fifth-field", "sixth-field", "seventh-field"}
                                )
                        }
                )
        );
        verifyReplicatedServices(expectedReplicatedServices);
    }

    @SuppressWarnings("unchecked")
    private Map<Object, ReplicationProducer> getProducersByServices(ReplicationProducerRegistry registry) {
        return getFieldValue(registry, FIELD_NAME_PRODUCERS_BY_SERVICES, Map.class);
    }

    private void verifyReplicatedServices(List<ReplicatedService> expected) {
        verify(mockedProducerFactory, times(expected.size())).create(replicatedServiceArgumentCaptor.capture());
        List<ReplicatedService> actual = replicatedServiceArgumentCaptor.getAllValues();
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }
}
