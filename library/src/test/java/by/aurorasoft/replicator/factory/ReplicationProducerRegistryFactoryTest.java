package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.testrepository.FirstTestRepository;
import by.aurorasoft.replicator.testrepository.SecondTestRepository;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static by.aurorasoft.replicator.testutil.ProducerConfigUtil.checkEquals;
import static by.aurorasoft.replicator.testutil.ProducerConfigUtil.createProducerConfig;
import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationProducerRegistryFactoryTest {
    private static final String FIELD_NAME_PRODUCERS_BY_REPOSITORIES = "producersByRepositories";

    @Mock
    private ReplicatedRepositoryRegistry mockedRepositoryRegistry;

    @Mock
    private ReplicationProducerFactory mockedProducerFactory;

    private ReplicationProducerRegistryFactory registryFactory;

    @Captor
    private ArgumentCaptor<Producer> producerConfigArgumentCaptor;

    @Before
    public void initializeRegistryFactory() {
        registryFactory = new ReplicationProducerRegistryFactory(mockedRepositoryRegistry, mockedProducerFactory);
    }

    @Test
    public void registryShouldBeCreated() {
        JpaRepository<?, ?> firstGivenRepository = new FirstTestRepository();
        JpaRepository<?, ?> secondGivenRepository = new SecondTestRepository();
        var givenRepositories = new LinkedHashSet<>(List.of(firstGivenRepository, secondGivenRepository));
        when(mockedRepositoryRegistry.getRepositories()).thenReturn(givenRepositories);

        ReplicationProducer firstGivenProducer = mockProducerCreation("first-topic");
        ReplicationProducer secondGivenProducer = mockProducerCreation("second-topic");

        ReplicationProducerRegistry actual = registryFactory.create();
        Map<Object, ReplicationProducer> actualProducersByRepositories = getProducersByRepositories(actual);
        Map<Object, ReplicationProducer> expectedProducersByRepositories = Map.of(
                firstGivenRepository, firstGivenProducer,
                secondGivenRepository, secondGivenProducer
        );
        assertEquals(expectedProducersByRepositories, actualProducersByRepositories);

        List<Producer> expectedProducerConfigs = List.of(
                createProducerConfig(LongSerializer.class, 10, 500, 100000),
                createProducerConfig(LongSerializer.class, 15, 515, 110000)
        );
        verifyProducerConfigs(expectedProducerConfigs);
    }

    private ReplicationProducer mockProducerCreation(String topicName) {
        ReplicationProducer producer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(eq(topicName), any(Producer.class))).thenReturn(producer);
        return producer;
    }

    @SuppressWarnings("unchecked")
    private Map<Object, ReplicationProducer> getProducersByRepositories(ReplicationProducerRegistry registry) {
        return getFieldValue(registry, FIELD_NAME_PRODUCERS_BY_REPOSITORIES, Map.class);
    }

    private void verifyProducerConfigs(List<Producer> expected) {
        verify(mockedProducerFactory, times(expected.size()))
                .create(anyString(), producerConfigArgumentCaptor.capture());
        List<Producer> actual = producerConfigArgumentCaptor.getAllValues();
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }
}
