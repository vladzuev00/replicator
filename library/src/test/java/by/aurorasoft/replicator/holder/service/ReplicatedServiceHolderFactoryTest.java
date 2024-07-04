package by.aurorasoft.replicator.holder.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
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
public final class ReplicatedServiceHolderFactoryTest {

    @Mock
    private ApplicationContext mockedContext;

    private ReplicatedServiceRegistryFactory factory;

    @Before
    public void initializeFactory() {
        factory = new ReplicatedServiceRegistryFactory(mockedContext);
    }

    @Test
    public void holderShouldBeCreated() {
        try (final MockedStatic<AopProxyUtils> mockedProxyUtil = mockStatic(AopProxyUtils.class)) {
            final Object firstGivenProxy = new Object();
            final Object secondGivenService = new Object();
            final Map<String, Object> givenServicesByNames = Map.of(
                    "FirstService", firstGivenProxy,
                    "SecondService", secondGivenService
            );
            when(mockedContext.getBeansWithAnnotation(same(ReplicatedService.class))).thenReturn(givenServicesByNames);

            final Object firstGivenService = mockServiceFor(firstGivenProxy, mockedProxyUtil);

            final ReplicatedServiceRegistry actual = factory.create();
            final Set<Object> actualServices = actual.getServices();
            final Set<Object> expectedServices = Set.of(firstGivenService, secondGivenService);
            assertEquals(expectedServices, actualServices);
        }
    }

    private Object mockServiceFor(final Object proxy, final MockedStatic<AopProxyUtils> mockedProxyUtil) {
        final Object service = new Object();
        mockedProxyUtil.when(() -> getSingletonTarget(same(proxy))).thenReturn(service);
        return service;
    }
}
