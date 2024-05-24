package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.util.IdUtil.NoIdGetterException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.junit.Test;

import static by.aurorasoft.replicator.util.IdUtil.getId;
import static org.junit.Assert.assertSame;

public final class IdUtilTest {

    @Test
    public void idShouldBeGot() {
        final Object givenId = new Object();
        final FirstTestObject givenObject = new FirstTestObject(givenId);

        final Object actual = getId(givenObject);
        assertSame(givenId, actual);
    }

    @Test(expected = NoIdGetterException.class)
    public void idShouldNotBeGotBecauseOfNoSuitableGetter() {
        final Object givenId = new Object();
        final SecondTestObject givenObject = new SecondTestObject(givenId);

        getId(givenObject);
    }

    @Value
    private static class FirstTestObject {
        Object id;
    }

    @RequiredArgsConstructor
    private static final class SecondTestObject {

        @SuppressWarnings("unused")
        private final Object id;
    }
}
