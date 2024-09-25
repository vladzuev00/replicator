package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.annotationprocessing.ElementUtil;
import by.aurorasoft.replicator.util.annotationprocessing.ExecutableElementUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedMethodAnnotationProcessor.INSIDE_REPLICATED_SERVICE_REQUIREMENT;
import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isReplicatedService;
import static by.aurorasoft.replicator.util.annotationprocessing.ExecutableElementUtil.getEnclosingClass;
import static java.util.Optional.empty;
import static java.util.stream.IntStream.range;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.type.TypeKind.DOUBLE;
import static javax.lang.model.type.TypeKind.VOID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public final class ReplicatedMethodAnnotationProcessorTest {
    private final ReplicatedMethodAnnotationProcessor processor = new TestReplicatedMethodAnnotationProcessor();

    @Test
    public void publicElementShouldBeValid() {
        try (MockedStatic<ExecutableElementUtil> mockedExecutableElementUtil = mockStatic(ExecutableElementUtil.class);
             MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            ExecutableElement givenElement = mock(ExecutableElement.class);

            mockEnclosingClass(givenElement, true, CLASS, mockedExecutableElementUtil, mockedElementUtil);
            mockReturnType(givenElement, VOID);
            mockParameterCount(givenElement, 1);

            assertTrue(processor.isValidPublicElement(givenElement));
        }
    }

    @Test
    public void publicElementShouldNotBeValidBecauseOfEnclosingClassIsNotReplicatedService() {
        try (MockedStatic<ExecutableElementUtil> mockedExecutableElementUtil = mockStatic(ExecutableElementUtil.class);
             MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            ExecutableElement givenElement = mock(ExecutableElement.class);

            mockEnclosingClass(givenElement, false, CLASS, mockedExecutableElementUtil, mockedElementUtil);

            assertFalse(processor.isValidPublicElement(givenElement));
        }
    }

    @Test
    public void publicElementShouldNotBeValidBecauseOfEnclosingClassIsNotValidReplicatedService() {
        try (MockedStatic<ExecutableElementUtil> mockedExecutableElementUtil = mockStatic(ExecutableElementUtil.class);
             MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            ExecutableElement givenElement = mock(ExecutableElement.class);

            mockEnclosingClass(givenElement, true, INTERFACE, mockedExecutableElementUtil, mockedElementUtil);

            assertFalse(processor.isValidPublicElement(givenElement));
        }
    }

    @Test
    public void publicElementShouldNotBeValidBecauseOfReturnTypeIsNotValid() {
        try (MockedStatic<ExecutableElementUtil> mockedExecutableElementUtil = mockStatic(ExecutableElementUtil.class);
             MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            ExecutableElement givenElement = mock(ExecutableElement.class);

            mockEnclosingClass(givenElement, true, CLASS, mockedExecutableElementUtil, mockedElementUtil);
            mockReturnType(givenElement, DOUBLE);

            assertFalse(processor.isValidPublicElement(givenElement));
        }
    }

    @Test
    public void publicElementShouldNotBeValidBecauseOfParametersIsNotValid() {
        try (MockedStatic<ExecutableElementUtil> mockedExecutableElementUtil = mockStatic(ExecutableElementUtil.class);
             MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            ExecutableElement givenElement = mock(ExecutableElement.class);

            mockEnclosingClass(givenElement, true, CLASS, mockedExecutableElementUtil, mockedElementUtil);
            mockReturnType(givenElement, VOID);
            mockParameterCount(givenElement, 0);

            assertFalse(processor.isValidPublicElement(givenElement));
        }
    }

    @Test
    public void requirementsShouldBeGotInternally() {
        List<String> actual = processor.getRequirementsInternal().toList();
        List<String> expected = List.of(
                INSIDE_REPLICATED_SERVICE_REQUIREMENT,
                TestReplicatedMethodAnnotationProcessor.REPLICATED_SERVICE_REQUIREMENT,
                TestReplicatedMethodAnnotationProcessor.PARAMETERS_REQUIREMENT
        );
        assertEquals(expected, actual);
    }

    private void mockEnclosingClass(ExecutableElement element,
                                    boolean replicatedService,
                                    ElementKind enclosingClassKind,
                                    MockedStatic<ExecutableElementUtil> executableElementUtil,
                                    MockedStatic<ElementUtil> mockedElementUtil) {
        TypeElement enclosingClass = mock(TypeElement.class);
        executableElementUtil.when(() -> getEnclosingClass(same(element))).thenReturn(enclosingClass);
        mockedElementUtil.when(() -> isReplicatedService(same(enclosingClass))).thenReturn(replicatedService);
        when(enclosingClass.getKind()).thenReturn(enclosingClassKind);
    }

    private void mockReturnType(ExecutableElement element, TypeKind kind) {
        TypeMirror returnType = mock(TypeMirror.class);
        when(returnType.getKind()).thenReturn(kind);
        when(element.getReturnType()).thenReturn(returnType);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void mockParameterCount(ExecutableElement element, int count) {
        List parameters = range(0, count)
                .mapToObj(i -> mock(VariableElement.class))
                .toList();
        when(element.getParameters()).thenReturn(parameters);
    }

    private @interface TestAnnotation {

    }

    private static final class TestReplicatedMethodAnnotationProcessor extends ReplicatedMethodAnnotationProcessor {
        private static final String REPLICATED_SERVICE_REQUIREMENT = "Replicated service's requirement";
        private static final String PARAMETERS_REQUIREMENT = "Parameters requirement";

        public TestReplicatedMethodAnnotationProcessor() {
            super(TestAnnotation.class);
        }

        @Override
        protected boolean isValidReplicatedService(TypeElement element) {
            return element.getKind() == CLASS;
        }

        @Override
        protected boolean isValidReturnType(TypeMirror mirror) {
            return mirror.getKind() == VOID;
        }

        @Override
        protected boolean isValidParameters(List<? extends VariableElement> elements) {
            return elements.size() == 1;
        }

        @Override
        protected Optional<String> getReplicatedServiceRequirement() {
            return Optional.of(REPLICATED_SERVICE_REQUIREMENT);
        }

        @Override
        protected Optional<String> getReturnTypeRequirement() {
            return empty();
        }

        @Override
        protected Optional<String> getParametersRequirement() {
            return Optional.of(PARAMETERS_REQUIREMENT);
        }
    }
}
