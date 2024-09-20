//package by.aurorasoft.replicator.annotation.processing.processor.operation;
//
//import by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import javax.lang.model.element.TypeElement;
//import javax.lang.model.element.VariableElement;
//import javax.lang.model.type.TypeMirror;
//import java.util.List;
//import java.util.Optional;
//
//import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteByIdsProcessor.PARAMETERS_REQUIREMENT;
//import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.isIterable;
//import static java.util.Collections.emptyList;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.mockStatic;
//
//public final class ReplicatedDeleteByIdsProcessorTest {
//    private final ReplicatedDeleteByIdsProcessor processor = new ReplicatedDeleteByIdsProcessor();
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
//            VariableElement givenElement = mock(VariableElement.class);
//            List<? extends VariableElement> givenElements = List.of(givenElement);
//
//            mockedProcessUtil.when(() -> isIterable(same(givenElement), any())).thenReturn(true);
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
//    public void parametersShouldNotBeValidBecauseOfFirstParameterIsNotIterable() {
//        try (MockedStatic<AnnotationProcessUtil> mockedProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
//            VariableElement givenElement = mock(VariableElement.class);
//            List<? extends VariableElement> givenElements = List.of(givenElement);
//
//            mockedProcessUtil.when(() -> isIterable(same(givenElement), any())).thenReturn(false);
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
