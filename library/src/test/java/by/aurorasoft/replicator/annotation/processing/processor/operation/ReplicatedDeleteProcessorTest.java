//package by.aurorasoft.replicator.annotation.processing.processor.operation;
//
//import by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import javax.annotation.processing.ProcessingEnvironment;
//import javax.lang.model.element.TypeElement;
//import javax.lang.model.element.VariableElement;
//import javax.lang.model.type.TypeMirror;
//import javax.lang.model.util.Elements;
//import java.util.List;
//import java.util.Optional;
//
//import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteProcessor.PARAMETERS_REQUIREMENT;
//import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.isContainIdGetter;
//import static java.util.Collections.emptyList;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public final class ReplicatedDeleteProcessorTest {
//    private final ReplicatedDeleteProcessor processor = new ReplicatedDeleteProcessor();
//
//    @Mock
//    private ProcessingEnvironment mockedEnvironment;
//
//    @BeforeEach
//    public void initializeProcessor() {
//        processor.init(mockedEnvironment);
//    }
//
//    @Test
//    public void replicatedServiceShouldBeValid() {
//        TypeElement givenElement = mock(TypeElement.class);
//
//        assertTrue(processor.isValidReplicatedService(givenElement));
//    }
//
//    @Test
//    public void returnTypeShouldBeValid() {
//        TypeMirror givenMirror = mock(TypeMirror.class);
//
//        assertTrue(processor.isValidReturnType(givenMirror));
//    }
//
//    @Test
//    public void parametersShouldBeValid() {
//        try (MockedStatic<AnnotationProcessUtil> mockedProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
//            VariableElement givenFirstParameter = mock(VariableElement.class);
//            List<? extends VariableElement> givenElements = List.of(givenFirstParameter);
//
//            Elements givenElementUtil = mock(Elements.class);
//            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);
//
//            mockedProcessUtil.when(() -> isContainIdGetter(same(givenFirstParameter), same(givenElementUtil)))
//                    .thenReturn(true);
//
//            assertTrue(processor.isValidParameters(givenElements));
//        }
//    }
//
//    @Test
//    public void parametersShouldNotBeValidBecauseOfNoParameters() {
//        try (MockedStatic<AnnotationProcessUtil> mockedProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
//            List<? extends VariableElement> givenElements = emptyList();
//
//            assertFalse(processor.isValidParameters(givenElements));
//
//            mockedProcessUtil.verifyNoInteractions();
//        }
//    }
//
//    @Test
//    public void parametersShouldNotBeValidBecauseOfFirstParameterNotContainId() {
//        try (MockedStatic<AnnotationProcessUtil> mockedProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
//            VariableElement givenFirstParameter = mock(VariableElement.class);
//            List<? extends VariableElement> givenElements = List.of(givenFirstParameter);
//
//            Elements givenElementUtil = mock(Elements.class);
//            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);
//
//            mockedProcessUtil.when(() -> isContainIdGetter(same(givenFirstParameter), same(givenElementUtil)))
//                    .thenReturn(false);
//
//            assertFalse(processor.isValidParameters(givenElements));
//        }
//    }
//
//    @Test
//    public void replicatedServiceRequirementShouldNotBeGot() {
//        Optional<String> optionalActual = processor.getReplicatedServiceRequirement();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void returnTypeRequirementShouldNotBeGot() {
//        Optional<String> optionalActual = processor.getReturnTypeRequirement();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void parametersRequirementShouldBeGot() {
//        Optional<String> optionalActual = processor.getParametersRequirement();
//        assertTrue(optionalActual.isPresent());
//        String actual = optionalActual.get();
//        assertSame(PARAMETERS_REQUIREMENT, actual);
//    }
//}
