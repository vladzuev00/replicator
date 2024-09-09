package by.aurorasoft.replicator.loader;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.testcrud.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ReplicatedServiceLoaderTest {

    @Mock
    private ApplicationContext mockedApplicationContext;

    private ReplicatedServiceLoader loader;

    @BeforeEach
    public void initializeLoader() {
        loader = new ReplicatedServiceLoader(mockedApplicationContext);
    }

    @Test
    public void servicesShouldBeLoaded() {
        TestService firstGivenService = new TestService(null);
        TestService secondGivenService = new TestService(null);

        Map<String, Object> givenBeansByNames = Map.of(
                "firstService", firstGivenService,
                "secondService", secondGivenService
        );
        when(mockedApplicationContext.getBeansWithAnnotation(same(ReplicatedService.class)))
                .thenReturn(givenBeansByNames);

        Set<Object> actual = new HashSet<>(loader.load());
        Set<Object> expected = Set.of(firstGivenService, secondGivenService);
        assertEquals(expected, actual);
    }
}
