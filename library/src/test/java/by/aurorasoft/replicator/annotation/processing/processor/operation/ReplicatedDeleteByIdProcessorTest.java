package by.aurorasoft.replicator.annotation.processing.processor.operation;

import org.junit.jupiter.api.Test;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteByIdProcessor.PARAMETERS_REQUIREMENT;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public final class ReplicatedDeleteByIdProcessorTest {
    private final ReplicatedDeleteByIdProcessor processor = new ReplicatedDeleteByIdProcessor();

    @Test
    public void replicatedServiceShouldBeValid() {
        TypeMirror givenMirror = mock(TypeMirror.class);

        boolean actual = processor.isValidReplicatedService(givenMirror);
        assertTrue(actual);
    }

    @Test
    public void returnTypeShouldBeValid() {
        TypeMirror givenMirror = mock(TypeMirror.class);

        boolean actual = processor.isValidReturnType(givenMirror);
        assertTrue(actual);
    }

    @Test
    public void parametersShouldBeValid() {
        List<? extends VariableElement> givenElements = singletonList(mock(VariableElement.class));

        boolean actual = processor.isValidParameters(givenElements);
        assertTrue(actual);
    }

    @Test
    public void parametersShouldNotBeValid() {
        List<? extends VariableElement> givenElements = emptyList();

        boolean actual = processor.isValidParameters(givenElements);
        assertFalse(actual);
    }

    @Test
    public void replicatedServiceRequirementShouldNotBeGot() {
        Optional<String> optionalActual = processor.getReplicatedServiceRequirement();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void returnTypeRequirementShouldNotBeGot() {
        Optional<String> optionalActual = processor.getReturnTypeRequirement();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void parametersRequirementShouldBeGot() {
        Optional<String> optionalActual = processor.getParametersRequirement();
        assertTrue(optionalActual.isPresent());
        String actual = optionalActual.get();
        assertSame(PARAMETERS_REQUIREMENT, actual);
    }
}
