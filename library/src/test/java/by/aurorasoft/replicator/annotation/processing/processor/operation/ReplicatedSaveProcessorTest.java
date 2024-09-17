package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedSaveProcessor.RETURN_TYPE_REQUIREMENT;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isContainIdGetter;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public final class ReplicatedSaveProcessorTest {
    private final ReplicatedSaveProcessor processor = new ReplicatedSaveProcessor();

    @Test
    public void replicatedServiceShouldBeValid() {
        TypeMirror givenMirror = mock(TypeMirror.class);

        assertTrue(processor.isValidReplicatedService(givenMirror));
    }

    @Test
    public void returnTypeShouldBeValid() {
        try (MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);

            mockedMirrorUtil.when(() -> isContainIdGetter(same(givenMirror), any())).thenReturn(true);

            assertTrue(processor.isValidReturnType(givenMirror));
        }
    }

    @Test
    public void returnTypeShouldNotBeValid() {
        try (MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);

            mockedMirrorUtil.when(() -> isContainIdGetter(same(givenMirror), any())).thenReturn(false);

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
