package by.aurorasoft.replicator.validator;

import lombok.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class UniquePropertyValidatorTest {
    private final TestUniquePropertyValidator validator = new TestUniquePropertyValidator();

    @Test
    public void sourcesShouldBeValid() {
        List<TestSource> givenSource = List.of(
                new TestSource("value-1", "value-2"),
                new TestSource("value-1", "value-3"),
                new TestSource("value-1", "value-4"),
                new TestSource("value-1", "value-5")
        );

        validator.validate(givenSource);
    }

    @Test
    public void sourcesShouldNotBeValid() {
        List<TestSource> givenSource = List.of(
                new TestSource("value-1", "value-2"),
                new TestSource("value-3", "value-4"),
                new TestSource("value-5", "value-4"),
                new TestSource("value-6", "value-7")
        );

        assertThrows(IllegalStateException.class, () -> validator.validate(givenSource));
    }

    private static final class TestUniquePropertyValidator extends UniquePropertyValidator<TestSource, String> {
        private static final String VIOLATION_MESSAGE = "Violation";

        public TestUniquePropertyValidator() {
            super(VIOLATION_MESSAGE);
        }

        @Override
        protected String getProperty(TestSource source) {
            return source.getSecondProperty();
        }
    }

    @Value
    private static class TestSource {
        String firstProperty;
        String secondProperty;
    }
}
