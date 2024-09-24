package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.annotationprocessing.ElementUtil;
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

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteByIdsProcessor.PARAMETERS_REQUIREMENT;
import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isIterable;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicatedDeleteByIdsProcessorTest {
    private final ReplicatedDeleteByIdsProcessor processor = new ReplicatedDeleteByIdsProcessor();

    @Mock
    private ProcessingEnvironment mockedEnvironment;

    @BeforeEach
    public void initializeProcessor() {
        processor.init(mockedEnvironment);
    }

    @Test
    public void replicatedServiceShouldBeValid() {
        TypeElement givenElement = mock(TypeElement.class);

        assertTrue(processor.isValidReplicatedService(givenElement));
    }

    @Test
    public void returnTypeShouldBeValid() {
        TypeMirror givenMirror = mock(TypeMirror.class);

        assertTrue(processor.isValidReturnType(givenMirror));
    }

    @Test
    public void parametersShouldBeValid() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenElement);

            Elements givenElementUtil = mock(Elements.class);
            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);

            Types givenTypeUtil = mock(Types.class);
            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);

            mockedElementUtil.when(() -> isIterable(same(givenElement), same(givenElementUtil), same(givenTypeUtil)))
                    .thenReturn(true);

            assertTrue(processor.isValidParameters(givenElements));
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfNoParameters() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            List<? extends VariableElement> givenElements = emptyList();

            assertFalse(processor.isValidParameters(givenElements));

            mockedElementUtil.verifyNoInteractions();
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfFirstParameterIsNotIterable() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenElement);

            Elements givenElementUtil = mock(Elements.class);
            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);

            Types givenTypeUtil = mock(Types.class);
            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);

            mockedElementUtil.when(() -> isIterable(same(givenElement), same(givenElementUtil), same(givenTypeUtil)))
                    .thenReturn(false);

            assertFalse(processor.isValidParameters(givenElements));
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
