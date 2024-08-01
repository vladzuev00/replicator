package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;

@ExtendWith(MockitoExtension.class)
public final class ReplicatedServiceRegistryFactoryTest {

    @Mock
    private ApplicationContext mockedContext;

    private ReplicatedRepositoryRegistryFactory factory;

    @BeforeEach
    public void initializeFactory() {
        factory = new ReplicatedRepositoryRegistryFactory(mockedContext);
    }

    @Test
    public void registryShouldBeCreated() {
        try (MockedStatic<AopProxyUtils> mockedProxyUtil = mockStatic(AopProxyUtils.class)) {
            Object firstGivenProxy = new Object();
            Object secondGivenService = new Object();
            Map<String, Object> givenServicesByNames = Map.of(
                    "FirstService", firstGivenProxy,
                    "SecondService", secondGivenService
            );
            when(mockedContext.getBeansWithAnnotation(same(ReplicatedRepository.class))).thenReturn(givenServicesByNames);

            Object firstGivenService = mockServiceFor(firstGivenProxy, mockedProxyUtil);

            ReplicatedRepositoryRegistry actual = factory.create();
//            Set<Object> actualServices = actual.getRepositories();
//            Set<Object> expectedServices = Set.of(firstGivenService, secondGivenService);
//            assertEquals(expectedServices, actualServices);
        }
    }

    private Object mockServiceFor(Object proxy, MockedStatic<AopProxyUtils> mockedProxyUtil) {
        Object service = new Object();
        mockedProxyUtil.when(() -> getSingletonTarget(same(proxy))).thenReturn(service);
        return service;
    }
}
