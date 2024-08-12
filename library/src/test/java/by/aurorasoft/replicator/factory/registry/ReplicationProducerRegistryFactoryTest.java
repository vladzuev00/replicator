package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.validator.ReplicationUniqueComponentCheckingManager;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerRegistryFactoryTest {
    private static final String FIELD_NAME_PRODUCERS_BY_REPOSITORIES = "producersByRepositories";

    @Mock
    private ReplicationUniqueComponentCheckingManager mockedUniqueComponentCheckingManager;

    @Mock
    private ReplicationProducerFactory mockedProducerFactory;

    private ReplicationProducerRegistryFactory registryFactory;

    @BeforeEach
    public void initializeRegistryFactory() {
        registryFactory = new ReplicationProducerRegistryFactory(
                mockedUniqueComponentCheckingManager,
                mockedProducerFactory
        );
    }

    @Test
    public void registryShouldBeCreated() {
        ReplicationProduceSetting<?, ?> firstGivenSetting = mock(ReplicationProduceSetting.class);
        ReplicationProduceSetting<?, ?> secondGivenSetting = mock(ReplicationProduceSetting.class);
        List<ReplicationProduceSetting<?, ?>> givenSettings = List.of(firstGivenSetting, secondGivenSetting);

        JpaRepository<?, ?> firstGivenRepository = mockRepositoryFor(firstGivenSetting);
        JpaRepository<?, ?> secondGivenRepository = mockRepositoryFor(secondGivenSetting);

        ReplicationProducer firstGivenProducer = mockProducerFor(firstGivenSetting);
        ReplicationProducer secondGivenProducer = mockProducerFor(secondGivenSetting);

        ReplicationProducerRegistry actual = registryFactory.create(givenSettings);

        var actualProducersByRepositories = getProducersByRepositories(actual);
        Map<JpaRepository<?, ?>, ReplicationProducer> expectedProducersByRepositories = Map.of(
                firstGivenRepository, firstGivenProducer,
                secondGivenRepository, secondGivenProducer
        );
        assertEquals(expectedProducersByRepositories, actualProducersByRepositories);

        verify(mockedUniqueComponentCheckingManager, times(1)).check(same(givenSettings));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private JpaRepository<?, ?> mockRepositoryFor(ReplicationProduceSetting<?, ?> setting) {
        JpaRepository repository = mock(JpaRepository.class);
        when(setting.getRepository()).thenReturn(repository);
        return repository;
    }

    private ReplicationProducer mockProducerFor(ReplicationProduceSetting<?, ?> setting) {
        ReplicationProducer producer = mock(ReplicationProducer.class);
        when(mockedProducerFactory.create(same(setting))).thenReturn(producer);
        return producer;
    }

    @SuppressWarnings("unchecked")
    private Map<JpaRepository<?, ?>, ReplicationProducer> getProducersByRepositories(ReplicationProducerRegistry registry) {
        return getFieldValue(registry, FIELD_NAME_PRODUCERS_BY_REPOSITORIES, Map.class);
    }
}
