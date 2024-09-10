package by.aurorasoft.replicator.annotation.processing.processor.service;

import org.junit.jupiter.api.Test;

import javax.lang.model.element.TypeElement;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public final class ReplicatedServiceProcessorTest {
    private final ReplicatedServiceProcessor processor = new ReplicatedServiceProcessor();

    @Test
    public void publicElementShouldBeValid() {
        TypeElement givenElement = mock(TypeElement.class);

        boolean actual = processor.isValidPublicElement(givenElement);
        assertTrue(actual);
    }

    @Test
    public void requirementsShouldBeGotInternally() {
        Stream<String> actual = processor.getRequirementsInternal();
        boolean actualEmpty = actual.findAny().isEmpty();
        assertTrue(actualEmpty);
    }
}