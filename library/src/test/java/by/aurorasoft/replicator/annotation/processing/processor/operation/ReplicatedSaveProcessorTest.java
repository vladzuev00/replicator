package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.AnnotationProcessUtil;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedSaveProcessor.RETURN_TYPE_REQUIREMENT;
import static by.aurorasoft.replicator.util.AnnotationProcessUtil.isContainIdGetter;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public final class ReplicatedSaveProcessorTest {
    private final ReplicatedSaveProcessor processor = new ReplicatedSaveProcessor();

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
            mockedAnnotationProcessUtil.when(() -> isContainIdGetter(givenMirror)).thenReturn(true);

            boolean actual = processor.isValidReplicatedService(givenMirror);
            assertTrue(actual);
        }
    }

    @Test
    public void returnTypeShouldNotBeValid() {
        try (var mockedAnnotationProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);

            //noinspection ResultOfMethodCallIgnored
            mockedAnnotationProcessUtil.when(() -> isContainIdGetter(givenMirror)).thenReturn(false);

            boolean actual = processor.isValidReplicatedService(givenMirror);
            assertTrue(actual);
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
