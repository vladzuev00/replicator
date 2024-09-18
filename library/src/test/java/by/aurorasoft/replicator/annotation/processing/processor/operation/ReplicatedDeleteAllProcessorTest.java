package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteAllProcessor.REPLICATED_SERVICE_REQUIREMENT;
import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.isContainRepository;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public final class ReplicatedDeleteAllProcessorTest {
    private final ReplicatedDeleteAllProcessor processor = new ReplicatedDeleteAllProcessor();

    @Test
    public void replicatedServiceShouldBeValid() {
        try (MockedStatic<AnnotationProcessUtil> mockedProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            TypeElement givenElement = mock(TypeElement.class);
            mockedProcessUtil.when(() -> isContainRepository(same(givenElement), any())).thenReturn(true);

            assertTrue(processor.isValidReplicatedService(givenElement));
        }
    }

    @Test
    public void replicatedServiceShouldNotBeValid() {
        try (MockedStatic<AnnotationProcessUtil> mockedProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            TypeElement givenElement = mock(TypeElement.class);
            mockedProcessUtil.when(() -> isContainRepository(same(givenElement), any())).thenReturn(false);

            assertFalse(processor.isValidReplicatedService(givenElement));
        }
    }

    @Test
    public void returnTypeShouldBeValid() {
        TypeElement givenElement = mock(TypeElement.class);

        assertTrue(processor.isValidReturnType(givenElement));
    }

    @Test
    public void parametersShouldBeValid() {
        List<VariableElement> givenElements = emptyList();

        assertTrue(processor.isValidParameters(givenElements));
    }

    @Test
    public void replicatedServiceRequirementShouldBeGot() {
        Optional<String> optionalActual = processor.getReplicatedServiceRequirement();
        assertTrue(optionalActual.isPresent());
        String actual = optionalActual.get();
        assertSame(REPLICATED_SERVICE_REQUIREMENT, actual);
    }

    @Test
    public void returnTypeRequirementShouldNotBeGot() {
        Optional<String> optionalActual = processor.getReturnTypeRequirement();
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void parametersRequirementShouldNotBeGot() {
        Optional<String> optionalActual = processor.getParametersRequirement();
        assertTrue(optionalActual.isEmpty());
    }
}
