package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.AnnotationProcessUtil;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteIterableProcessor.PARAMETERS_REQUIREMENT;
import static by.aurorasoft.replicator.util.AnnotationProcessUtil.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
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
        try (var mockedAnnotationProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenElement);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> isIterable(same(givenElement))).thenReturn(true);

            TypeMirror givenIterableGenericType = mock(TypeMirror.class);
            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> getFirstGenericParameterType(givenElement))
                    .thenReturn(givenIterableGenericType);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> isContainIdGetter(same(givenIterableGenericType))).thenReturn(true);

            boolean actual = processor.isValidParameters(givenElements);
            assertTrue(actual);
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfNoParameters() {
        try (var mockedAnnotationProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            List<? extends VariableElement> givenElements = emptyList();

            boolean actual = processor.isValidParameters(givenElements);
            assertFalse(actual);

            mockedAnnotationProcessUtil.verifyNoInteractions();
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfFirstParameterIsNotIterable() {
        try (var mockedAnnotationProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenElement);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> isIterable(same(givenElement))).thenReturn(false);

            boolean actual = processor.isValidParameters(givenElements);
            assertFalse(actual);

            mockedAnnotationProcessUtil.verify(() -> getFirstGenericParameterType(any(TypeMirror.class)), times(0));
            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.verify(() -> isContainIdGetter(any(TypeMirror.class)), times(0));
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfFirstGenericParameterTypeNotContainId() {
        try (var mockedAnnotationProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenElement);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> isIterable(same(givenElement))).thenReturn(true);

            TypeMirror givenIterableGenericType = mock(TypeMirror.class);
            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> getFirstGenericParameterType(givenElement))
                    .thenReturn(givenIterableGenericType);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> isContainIdGetter(same(givenIterableGenericType))).thenReturn(false);

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
