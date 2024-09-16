package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.ElementUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteProcessor.PARAMETERS_REQUIREMENT;
import static by.aurorasoft.replicator.util.ElementUtil.isContainIdGetter;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public final class ReplicatedDeleteProcessorTest {
    private final ReplicatedDeleteProcessor processor = new ReplicatedDeleteProcessor();

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
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            VariableElement givenFirstParameter = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenFirstParameter);

            mockedElementUtil.when(() -> isContainIdGetter(same(givenFirstParameter))).thenReturn(true);

            boolean actual = processor.isValidParameters(givenElements);
            assertTrue(actual);
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfNoParameters() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            List<? extends VariableElement> givenElements = emptyList();

            boolean actual = processor.isValidParameters(givenElements);
            assertFalse(actual);

            mockedElementUtil.verifyNoInteractions();
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfFirstParameterNotContainId() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            VariableElement givenFirstParameter = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenFirstParameter);

            mockedElementUtil.when(() -> isContainIdGetter(same(givenFirstParameter))).thenReturn(false);

            boolean actual = processor.isValidParameters(givenElements);
            assertFalse(actual);
        }
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
