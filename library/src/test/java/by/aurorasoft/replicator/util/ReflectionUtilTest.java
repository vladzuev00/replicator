package by.aurorasoft.replicator.util;

import lombok.Value;
import org.junit.jupiter.api.Test;

import static by.aurorasoft.replicator.testutil.AssertExceptionUtil.executeExpectingException;
import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ReflectionUtilTest {

    @Test
    public void fieldValueShouldBeGot() {
        Object givenProperty = new Object();
        Object givenObject = new TestObject(givenProperty);
        String givenFieldName = "property";
        Class<Object> givenType = Object.class;

        Object actual = getFieldValue(givenObject, givenFieldName, givenType);
        assertSame(givenProperty, actual);
    }

    @Test
    public void fieldValueShouldNotBeGot() {
        Object givenObject = new TestObject(new Object());
        String givenFieldName = "notExistingProperty";
        Class<Object> givenType = Object.class;

        executeExpectingException(
                () -> getFieldValue(givenObject, givenFieldName, givenType),
                NullPointerException.class,
                "There is no field 'notExistingProperty' in 'class by.aurorasoft.replicator.util.ReflectionUtilTest$TestObject'"
        );
    }

    @Value
    private static class TestObject {
        Object property;
    }
}
