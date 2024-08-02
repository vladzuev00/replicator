package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import by.aurorasoft.replicator.util.ProxyUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Set;

import static by.aurorasoft.replicator.util.ProxyUtil.unProxy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicatedRepositoryRegistryFactoryTest {

    @Mock
    private ApplicationContext mockedContext;

    private ReplicatedRepositoryRegistryFactory factory;

    @BeforeEach
    public void initializeFactory() {
        factory = new ReplicatedRepositoryRegistryFactory(mockedContext);
    }

    @Test
    public void registryShouldBeCreated() {
        try (MockedStatic<ProxyUtil> mockedProxyUtil = mockStatic(ProxyUtil.class)) {
            Object firstGivenBean = new Object();
            Object secondGivenBean = new Object();
            Map<String, Object> givenBeansByNames = Map.of(
                    "FirstRepository", firstGivenBean,
                    "SecondRepository", secondGivenBean
            );
            when(mockedContext.getBeansWithAnnotation(same(ReplicatedRepository.class))).thenReturn(givenBeansByNames);

            JpaRepository<?, ?> firstGivenRepository = mockRepositoryFor(firstGivenBean, mockedProxyUtil);
            JpaRepository<?, ?> secondGivenRepository = mockRepositoryFor(secondGivenBean, mockedProxyUtil);

            ReplicatedRepositoryRegistry actual = factory.create();
            Set<JpaRepository<?, ?>> actualRepositories = actual.getRepositories();
            Set<JpaRepository<?, ?>> expectedRepositories = Set.of(firstGivenRepository, secondGivenRepository);
            assertEquals(expectedRepositories, actualRepositories);
        }
    }

    private JpaRepository<?, ?> mockRepositoryFor(Object proxy, MockedStatic<ProxyUtil> mockedUtil) {
        JpaRepository<?, ?> repository = mock(JpaRepository.class);
        mockedUtil.when(() -> unProxy(same(proxy))).thenReturn(repository);
        return repository;
    }
}
