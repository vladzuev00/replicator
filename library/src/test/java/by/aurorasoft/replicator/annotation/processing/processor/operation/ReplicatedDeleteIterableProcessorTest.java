package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.ElementUtil;
import by.aurorasoft.replicator.util.TypeMirrorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteIterableProcessor.PARAMETERS_REQUIREMENT;
import static by.aurorasoft.replicator.util.ElementUtil.getFirstTypeArgument;
import static by.aurorasoft.replicator.util.ElementUtil.isIterable;
import static by.aurorasoft.replicator.util.TypeMirrorUtil.isContainIdGetter;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class ReplicatedDeleteIterableProcessorTest {
    private final ReplicatedDeleteIterableProcessor processor = new ReplicatedDeleteIterableProcessor();

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
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class);
             MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            VariableElement givenFirstParameter = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenFirstParameter);

            mockedElementUtil.when(() -> isIterable(same(givenFirstParameter), any())).thenReturn(true);

            TypeMirror givenFirstTypeArgument = mock(TypeMirror.class);
            mockedElementUtil.when(() -> getFirstTypeArgument(same(givenFirstParameter)))
                    .thenReturn(givenFirstTypeArgument);
            mockedMirrorUtil.when(() -> isContainIdGetter(same(givenFirstTypeArgument), any())).thenReturn(true);

            boolean actual = processor.isValidParameters(givenElements);
            assertTrue(actual);
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfNoParameters() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class);
             MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            List<? extends VariableElement> givenElements = emptyList();

            boolean actual = processor.isValidParameters(givenElements);
            assertFalse(actual);

            mockedElementUtil.verifyNoInteractions();
            mockedMirrorUtil.verifyNoInteractions();
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfFirstParameterIsNotIterable() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class);
             MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            VariableElement givenFirstParameter = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenFirstParameter);

            mockedElementUtil.when(() -> isIterable(same(givenFirstParameter), any())).thenReturn(false);

            boolean actual = processor.isValidParameters(givenElements);
            assertFalse(actual);

            mockedElementUtil.verify(() -> getFirstTypeArgument(any(Element.class)), times(0));
            mockedMirrorUtil.verifyNoInteractions();
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfFirstTypeArgumentNotContainId() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class);
             MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            VariableElement givenFirstParameter = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenFirstParameter);

            mockedElementUtil.when(() -> isIterable(same(givenFirstParameter), any())).thenReturn(true);

            TypeMirror givenFirstTypeArgument = mock(TypeMirror.class);
            mockedElementUtil.when(() -> getFirstTypeArgument(same(givenFirstParameter)))
                    .thenReturn(givenFirstTypeArgument);
            mockedMirrorUtil.when(() -> isContainIdGetter(same(givenFirstTypeArgument), any())).thenReturn(false);

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
        Assertions.assertSame(PARAMETERS_REQUIREMENT, actual);
    }
}
