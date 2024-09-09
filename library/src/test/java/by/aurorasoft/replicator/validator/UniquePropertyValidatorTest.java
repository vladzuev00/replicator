package by.aurorasoft.replicator.validator;

import lombok.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class UniquePropertyValidatorTest {
    private final TestUniquePropertyValidator validator = new TestUniquePropertyValidator();

    @Test
    public void sourcesShouldBeValid() {
        List<TestSource> givenSources = List.of(
                new TestSource("value-1", "value-2"),
                new TestSource("value-1", "value-3"),
                new TestSource("value-1", "value-4"),
                new TestSource("value-1", "value-5")
        );

        validator.validate(givenSources);
    }

    @Test
    public void sourcesShouldNotBeValid() {
        List<TestSource> givenSources = List.of(
                new TestSource("value-1", "value-2"),
                new TestSource("value-3", "value-4"),
                new TestSource("value-5", "value-4"),
                new TestSource("value-6", "value-2")
        );

        validateExpectingException(
                givenSources,
                """
                        Violation
                        	Duplicates: [value-2, value-4]"""
        );
    }

    private void validateExpectingException(List<TestSource> sources,
                                            @SuppressWarnings("SameParameterValue") String expectedMessage) {
        boolean exceptionArisen = false;
        try {
            validator.validate(sources);
        } catch (IllegalStateException exception) {
            exceptionArisen = true;
            String actualMessage = exception.getMessage();
            assertEquals(expectedMessage, actualMessage);
        }
        assertTrue(exceptionArisen);
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
