package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.AnnotationProcessUtil;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedSaveAllProcessor.RETURN_TYPE_REQUIREMENT;
import static by.aurorasoft.replicator.util.AnnotationProcessUtil.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class ReplicatedSaveAllProcessorTest {
    private final ReplicatedSaveAllProcessor processor = new ReplicatedSaveAllProcessor();

    @Test
    public void replicatedServiceShouldBeValid() {
        TypeMirror givenMirror = mock(TypeMirror.class);

        boolean actual = processor.isValidReplicatedService(givenMirror);
        assertTrue(actual);
    }

    @Test
    public void returnTypeShouldBeValid() {
        try (var mockedAnnotationProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> isList(same(givenMirror))).thenReturn(true);

            TypeMirror givenListGenericType = mock(TypeMirror.class);
            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> getFirstGenericParameterType(same(givenMirror)))
                    .thenReturn(givenListGenericType);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> isContainIdGetter(same(givenListGenericType))).thenReturn(true);

            boolean actual = processor.isValidReturnType(givenMirror);
            assertTrue(actual);
        }
    }

    @Test
    public void returnTypeShouldNotBeValidBecauseOfNotList() {
        try (var mockedAnnotationProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> isList(same(givenMirror))).thenReturn(false);

            boolean actual = processor.isValidReturnType(givenMirror);
            assertFalse(actual);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.verify(() -> getFirstGenericParameterType(any(TypeMirror.class)), times(0));
            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.verify(() -> isContainIdGetter(any(TypeMirror.class)), times(0));
        }
    }

    @Test
    public void returnTypeShouldNotBeValidBecauseOfListGenericTypeNotContainId() {
        try (var mockedAnnotationProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> isList(same(givenMirror))).thenReturn(true);

            TypeMirror givenListGenericType = mock(TypeMirror.class);
            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> getFirstGenericParameterType(same(givenMirror)))
                    .thenReturn(givenListGenericType);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> isContainIdGetter(same(givenListGenericType))).thenReturn(false);

            boolean actual = processor.isValidReturnType(givenMirror);
            assertFalse(actual);
        }
    }

    @Test
    public void parametersShouldBeValid() {
        List<? extends VariableElement> givenElements = emptyList();

        boolean actual = processor.isValidParameters(givenElements);
        assertTrue(actual);
    }

    @Test
    public void replicatedServiceRequirementShouldNotBeGot() {
        Optional<String> optionalActual = processor.getReplicatedServiceRequirement();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void returnTypeRequirementShouldBeGot() {
        Optional<String> optionalActual = processor.getReturnTypeRequirement();
        assertTrue(optionalActual.isPresent());
        String actual = optionalActual.get();
        assertSame(RETURN_TYPE_REQUIREMENT, actual);
    }

    @Test
    public void parametersRequirementShouldNotBeGot() {
        Optional<String> optionalActual = processor.getParametersRequirement();
        assertTrue(optionalActual.isEmpty());
    }
}
