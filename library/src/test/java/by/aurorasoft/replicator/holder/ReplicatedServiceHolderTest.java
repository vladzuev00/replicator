package by.aurorasoft.replicator.holder;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.base.v2.service.FirstTestCRUDService;
import by.aurorasoft.replicator.base.v2.service.SecondTestCRUDService;
import by.aurorasoft.replicator.base.v2.service.ThirdTestCRUDService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNullElse;
import static org.junit.Assert.assertEquals;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;

public final class ReplicatedServiceHolderTest extends AbstractSpringBootTest {

    @Autowired
    private FirstTestCRUDService firstService;

    @Autowired
    private SecondTestCRUDService secondService;

    @Autowired
    private ThirdTestCRUDService thirdService;

    @Test
    public void holderShouldBeCreated() {
        final ReplicatedServiceHolder actual = new ReplicatedServiceHolder(
                List.of(
                        firstService,
                        secondService,
                        thirdService
                )
        );

        final Set<?> actualServices = new HashSet<>(actual.getServices());
        final Set<?> expectedServices = Set.of(unProxy(firstService), unProxy(secondService));
        assertEquals(expectedServices, actualServices);
    }

    private Object unProxy(final Object object) {
        return requireNonNullElse(getSingletonTarget(object), object);
    }
}
