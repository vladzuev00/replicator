package by.aurorasoft.replicator.util;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static by.aurorasoft.replicator.util.AnnotationUtil.getAnnotation;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AnnotationUtilTest {

    @Test
    public void annotationShouldBeGot() {
        TestFirstAnnotation actual = getAnnotation(TestObject.class, TestFirstAnnotation.class);
        String actualValue = actual.value();
        String expectedValue = "test-value";
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void annotationShouldNotBeGot() {
        assertThrows(NullPointerException.class, () -> getAnnotation(TestObject.class, TestSecondAnnotation.class));
    }

    @Target(TYPE)
    @Retention(RUNTIME)
    private @interface TestFirstAnnotation {
        String value();
    }

    @Target(TYPE)
    @Retention(RUNTIME)
    private @interface TestSecondAnnotation {
        String value();
    }

    @TestFirstAnnotation("test-value")
    private interface TestInterface {

    }

    private static final class TestObject implements TestInterface {

    }
}
