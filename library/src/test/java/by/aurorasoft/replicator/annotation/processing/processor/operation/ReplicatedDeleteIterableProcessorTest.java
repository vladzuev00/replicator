package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.annotationprocessing.ElementUtil;
import by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteIterableProcessor.PARAMETERS_REQUIREMENT;
import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.getFirstTypeArgument;
import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isIterable;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isContainIdGetter;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicatedDeleteIterableProcessorTest {
    private final ReplicatedDeleteIterableProcessor processor = new ReplicatedDeleteIterableProcessor();

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
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class);
             MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            VariableElement givenFirstParameter = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenFirstParameter);

            Elements givenElementUtil = mock(Elements.class);
            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);

            Types givenTypeUtil = mock(Types.class);
            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);

            mockedElementUtil.when(
                    () -> isIterable(
                            same(givenFirstParameter),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            TypeMirror givenFirstTypeArgument = mock(TypeMirror.class);
            mockedElementUtil.when(() -> getFirstTypeArgument(same(givenFirstParameter)))
                    .thenReturn(givenFirstTypeArgument);
            mockedTypeMirrorUtil.when(
                    () -> isContainIdGetter(
                            same(givenFirstTypeArgument),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(processor.isValidParameters(givenElements));
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfNoParameters() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class);
             MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            List<? extends VariableElement> givenElements = emptyList();

            assertFalse(processor.isValidParameters(givenElements));

            mockedElementUtil.verifyNoInteractions();
            mockedTypeMirrorUtil.verifyNoInteractions();
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfFirstParameterIsNotIterable() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class);
             MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            VariableElement givenFirstParameter = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenFirstParameter);

            Elements givenElementUtil = mock(Elements.class);
            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);

            Types givenTypeUtil = mock(Types.class);
            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);

            mockedElementUtil.when(
                    () -> isIterable(
                            same(givenFirstParameter),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(processor.isValidParameters(givenElements));

            mockedElementUtil.verify(() -> getFirstTypeArgument(any(Element.class)), times(0));
            mockedTypeMirrorUtil.verifyNoInteractions();
        }
    }

    @Test
    public void parametersShouldNotBeValidBecauseOfFirstTypeArgumentNotContainId() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class);
             MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            VariableElement givenFirstParameter = mock(VariableElement.class);
            List<? extends VariableElement> givenElements = List.of(givenFirstParameter);

            Elements givenElementUtil = mock(Elements.class);
            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);

            Types givenTypeUtil = mock(Types.class);
            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);

            mockedElementUtil.when(
                    () -> isIterable(
                            same(givenFirstParameter),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            TypeMirror givenFirstTypeArgument = mock(TypeMirror.class);
            mockedElementUtil.when(() -> getFirstTypeArgument(same(givenFirstParameter)))
                    .thenReturn(givenFirstTypeArgument);
            mockedTypeMirrorUtil.when(
                    () -> isContainIdGetter(
                            same(givenFirstTypeArgument),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

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
