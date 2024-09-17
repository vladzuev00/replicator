package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedSaveAllProcessor.RETURN_TYPE_REQUIREMENT;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.*;
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

        assertTrue(processor.isValidReplicatedService(givenMirror));
    }

    @Test
    public void returnTypeShouldBeValid() {
        try (MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);

            mockedMirrorUtil.when(() -> isList(same(givenMirror), any())).thenReturn(true);

            TypeMirror givenFirstTypeArgument = mock(TypeMirror.class);
            mockedMirrorUtil.when(() -> getFirstTypeArgument(same(givenMirror))).thenReturn(givenFirstTypeArgument);
            mockedMirrorUtil.when(() -> isContainIdGetter(same(givenFirstTypeArgument), any())).thenReturn(true);

            assertTrue(processor.isValidReturnType(givenMirror));
        }
    }

    @Test
    public void returnTypeShouldNotBeValidBecauseOfNotList() {
        try (MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);

            mockedMirrorUtil.when(() -> isList(same(givenMirror), any())).thenReturn(false);

            assertFalse(processor.isValidReturnType(givenMirror));

            mockedMirrorUtil.verify(() -> getFirstTypeArgument(any(TypeMirror.class)), times(0));
            mockedMirrorUtil.verify(() -> isContainIdGetter(any(TypeMirror.class), any()), times(0));
        }
    }

    @Test
    public void returnTypeShouldNotBeValidBecauseOfListTypeArgumentNotContainId() {
        try (MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);

            mockedMirrorUtil.when(() -> isList(same(givenMirror), any())).thenReturn(true);

            TypeMirror givenFirstTypeArgument = mock(TypeMirror.class);
            mockedMirrorUtil.when(() -> getFirstTypeArgument(same(givenMirror))).thenReturn(givenFirstTypeArgument);
            mockedMirrorUtil.when(() -> isContainIdGetter(same(givenFirstTypeArgument), any())).thenReturn(false);

            assertFalse(processor.isValidReturnType(givenMirror));
        }
    }

    @Test
    public void parametersShouldBeValid() {
        List<? extends VariableElement> givenElements = emptyList();

        assertTrue(processor.isValidParameters(givenElements));
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
