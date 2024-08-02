package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static by.aurorasoft.replicator.util.ProxyUtil.unProxy;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ProxyUtilTest extends AbstractSpringBootTest {

    @Autowired
    private TestProxy proxy;

    @Test
    public void proxiedObjectShouldBeUnProxied() {
        Object actual = unProxy(proxy);
        Class<?> actualType = actual.getClass();
        Class<?> expectedType = TestProxy.class;
        assertSame(expectedType, actualType);
    }

    @Test
    public void notProxiedObjectShouldBeUnProxied() {
        Object givenObject = new TestProxy();

        Object actual = unProxy(givenObject);
        assertSame(givenObject, actual);
    }

    @Component
    public static class TestProxy {

        @Transactional
        @SuppressWarnings("unused")
        public void execute() {
            throw new UnsupportedOperationException();
        }
    }
}
