package by.aurorasoft.replicator.registry;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class RegistryTest {
    private final TestRegistry registry = new TestRegistry();

    @Test
    public void valueShouldBeGot() {
        String givenKey = "3";

        Optional<Integer> optionalActual = registry.get(givenKey);
        assertTrue(optionalActual.isPresent());
        Integer actual = optionalActual.get();
        Integer expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void valueShouldNotBeGot() {
        String givenKey = "6";

        Optional<Integer> optionalActual = registry.get(givenKey);
        assertTrue(optionalActual.isEmpty());
    }

    private static final class TestRegistry extends Registry<String, Integer> {
        private static final Map<String, Integer> VALUES_BY_KEYS = Map.of(
                "1", 1,
                "2", 2,
                "3", 3,
                "4", 4,
                "5", 5
        );

        public TestRegistry() {
            super(VALUES_BY_KEYS);
        }
    }
}
