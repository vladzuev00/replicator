package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.util.ElementUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.ElementUtil.isReplicatedService;
import static java.util.Optional.empty;
import static javax.lang.model.type.TypeKind.DECLARED;
import static javax.lang.model.type.TypeKind.VOID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public final class ReplicatedMethodAnnotationProcessorTest {
    private final ReplicatedMethodAnnotationProcessor processor = new TestReplicatedMethodAnnotationProcessor();

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void elementShouldBeValid() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            ExecutableElement givenElement = mock(ExecutableElement.class);

            TypeMirror givenEnclosingElementMirror = createMirror(DECLARED);
            Element givenEnclosingElement = createElement(givenEnclosingElementMirror);
            when(givenElement.getEnclosingElement()).thenReturn(givenEnclosingElement);
            mockedElementUtil.when(() -> isReplicatedService(same(givenEnclosingElement))).thenReturn(true);

            TypeMirror givenReturnType = createMirror(VOID);
            when(givenElement.getReturnType()).thenReturn(givenReturnType);

            List givenParameters = List.of(mock(VariableElement.class));
            when(givenElement.getParameters()).thenReturn(givenParameters);

            boolean actual = processor.isValidPublicElement(givenElement);
            assertTrue(actual);
        }
    }

    @Test
    public void elementShouldNotBeValidBecauseOfEnclosingClassIsNotReplicatedService() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            ExecutableElement givenElement = mock(ExecutableElement.class);

            TypeMirror givenEnclosingElementMirror = mock(TypeMirror.class);
            Element givenEnclosingElement = createElement(givenEnclosingElementMirror);
            throw new UnsupportedOperationException();
        }
    }

    private TypeMirror createMirror(TypeKind kind) {
        TypeMirror mirror = mock(TypeMirror.class);
        when(mirror.getKind()).thenReturn(kind);
        return mirror;
    }

    private Element createElement(TypeMirror mirror) {
        Element element = mock(Element.class);
        when(element.asType()).thenReturn(mirror);
        return element;
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
        protected boolean isValidReplicatedService(TypeMirror mirror) {
            return mirror.getKind() == DECLARED;
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
