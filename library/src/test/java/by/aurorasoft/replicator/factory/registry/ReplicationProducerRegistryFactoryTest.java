package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerRegistryFactoryTest {
    private static final String FIELD_NAME_PRODUCERS_BY_REPOSITORIES = "producersByRepositories";

    @Mock
    private ReplicationProducerFactory mockedProducerFactory;

    @Mock
    private ReplicationProducerSetting<?, ?> mockedFirstProducerSetting;

    @Mock
    private ReplicationProducerSetting<?, ?> mockedSecondProducerSetting;

    private ReplicationProducerRegistryFactory registryFactory;

    @BeforeEach
    public void initializeRegistryFactory() {
        registryFactory = new ReplicationProducerRegistryFactory(
                mockedProducerFactory,
                List.of(mockedFirstProducerSetting, mockedSecondProducerSetting)
        );
    }

    @Test
    public void registryShouldBeCreated() {
        JpaRepository<?, ?> firstGivenRepository = mockRepositoryFor(mockedFirstProducerSetting);
        JpaRepository<?, ?> secondGivenRepository = mockRepositoryFor(mockedSecondProducerSetting);

        ReplicationProducer firstGivenProducer = mockProducerFor(mockedFirstProducerSetting);
        ReplicationProducer secondGivenProducer = mockProducerFor(mockedSecondProducerSetting);

        ReplicationProducerRegistry actual = registryFactory.create();

        var actualProducersByRepositories = getProducersByRepositories(actual);
        Map<JpaRepository<?, ?>, ReplicationProducer> expectedProducersByRepositories = Map.of(
                firstGivenRepository, firstGivenProducer,
                secondGivenRepository, secondGivenProducer
        );
        assertEquals(expectedProducersByRepositories, actualProducersByRepositories);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private JpaRepository<?, ?> mockRepositoryFor(ReplicationProducerSetting<?, ?> setting) {
        JpaRepository repository = mock(JpaRepository.class);
        when(setting.getRepository()).thenReturn(repository);
        return repository;
    }

    private ReplicationProducer mockProducerFor(ReplicationProducerSetting<?, ?> setting) {
        ReplicationProducer producer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(same(setting))).thenReturn(producer);
        return producer;
    }

    @SuppressWarnings("unchecked")
    private Map<JpaRepository<?, ?>, ReplicationProducer> getProducersByRepositories(ReplicationProducerRegistry registry) {
        return getFieldValue(registry, FIELD_NAME_PRODUCERS_BY_REPOSITORIES, Map.class);
    }
}
