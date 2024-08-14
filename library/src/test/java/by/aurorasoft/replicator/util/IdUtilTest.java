package by.aurorasoft.replicator.util;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.Test;

import static by.aurorasoft.replicator.util.PropertyUtil.getId;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class IdUtilTest {

    @Test
    public void idShouldBeGot() {
        Object givenId = new Object();
        FirstTestObject givenObject = new FirstTestObject(givenId);

        Object actual = getId(givenObject);
        assertSame(givenId, actual);
    }

    @Test
    public void idShouldNotBeGotBecauseOfNoGetter() {
        Object givenId = new Object();
        SecondTestObject givenObject = new SecondTestObject(givenId);

        assertThrows(NullPointerException.class, () -> getId(givenObject));
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
