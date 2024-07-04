package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.registry.ReplicatedServiceRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicatedServiceRegistryFactoryTest {

    @Mock
    private ApplicationContext mockedContext;

    private ReplicatedServiceRegistryFactory factory;

    @Before
    public void initializeFactory() {
        factory = new ReplicatedServiceRegistryFactory(mockedContext);
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
            when(mockedContext.getBeansWithAnnotation(same(ReplicatedService.class))).thenReturn(givenServicesByNames);

            Object firstGivenService = mockServiceFor(firstGivenProxy, mockedProxyUtil);

            ReplicatedServiceRegistry actual = factory.create();
            Set<Object> actualServices = actual.getServices();
            Set<Object> expectedServices = Set.of(firstGivenService, secondGivenService);
            assertEquals(expectedServices, actualServices);
        }
    }

    private Object mockServiceFor(Object proxy, MockedStatic<AopProxyUtils> mockedProxyUtil) {
        Object service = new Object();
        mockedProxyUtil.when(() -> getSingletonTarget(same(proxy))).thenReturn(service);
        return service;
    }
}
