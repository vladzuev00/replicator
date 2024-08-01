package by.aurorasoft.replicator.util;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.junit.Test;

import static by.aurorasoft.replicator.util.IdUtil.getId;
import static org.junit.Assert.assertSame;

public final class IdUtilTest {

    @Test
    public void idShouldBeGot() {
        Object givenId = new Object();
        FirstTestObject givenObject = new FirstTestObject(givenId);

        Object actual = getId(givenObject);
        assertSame(givenId, actual);
    }

    @Test(expected = NullPointerException.class)
    public void idShouldNotBeGotBecauseOfNoGetter() {
        Object givenId = new Object();
        SecondTestObject givenObject = new SecondTestObject(givenId);

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
