package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil;
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

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedSaveProcessor.RETURN_TYPE_REQUIREMENT;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isContainIdGetter;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicatedSaveProcessorTest {
    private final ReplicatedSaveProcessor processor = new ReplicatedSaveProcessor();

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
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);

            Elements givenElementUtil = mock(Elements.class);
            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);

            Types givenTypeUtil = mock(Types.class);
            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);

            mockedTypeMirrorUtil.when(
                    () -> isContainIdGetter(
                            same(givenMirror),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(processor.isValidReturnType(givenMirror));
        }
    }

    @Test
    public void returnTypeShouldNotBeValid() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);

            Elements givenElementUtil = mock(Elements.class);
            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);

            Types givenTypeUtil = mock(Types.class);
            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);

            mockedTypeMirrorUtil.when(
                    () -> isContainIdGetter(
                            same(givenMirror),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(processor.isValidReturnType(givenMirror));
        }
    }

    @Test
    public void parametersShouldBeValid() {
        List<VariableElement> givenElements = emptyList();

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
