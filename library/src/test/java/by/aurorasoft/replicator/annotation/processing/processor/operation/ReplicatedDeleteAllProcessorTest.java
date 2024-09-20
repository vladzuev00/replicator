package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteAllProcessor.REPLICATED_SERVICE_REQUIREMENT;
import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.isContainRepository;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicatedDeleteAllProcessorTest {
    private final ReplicatedDeleteAllProcessor processor = new ReplicatedDeleteAllProcessor();

    @Mock
    private ProcessingEnvironment mockedEnvironment;

    @BeforeEach
    public void initializeProcessor() {
        processor.init(mockedEnvironment);
    }

    @Test
    public void replicatedServiceShouldBeValid() {
        try (MockedStatic<AnnotationProcessUtil> mockedProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            TypeElement givenElement = mock(TypeElement.class);

            Elements givenElementUtil = mock(Elements.class);
            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);

            Types givenTypeUtil = mock(Types.class);
            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);

            mockedProcessUtil.when(
                    () -> isContainRepository(
                            same(givenElement),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(processor.isValidReplicatedService(givenElement));
        }
    }

    @Test
    public void replicatedServiceShouldNotBeValid() {
        try (MockedStatic<AnnotationProcessUtil> mockedProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
            TypeElement givenElement = mock(TypeElement.class);

            Elements givenElementUtil = mock(Elements.class);
            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);

            Types givenTypeUtil = mock(Types.class);
            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);

            mockedProcessUtil.when(
                    () -> isContainRepository(
                            same(givenElement),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(processor.isValidReplicatedService(givenElement));
        }
    }

    @Test
    public void returnTypeShouldBeValid() {
        TypeMirror givenMirror = mock(TypeMirror.class);

        assertTrue(processor.isValidReturnType(givenMirror));
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
