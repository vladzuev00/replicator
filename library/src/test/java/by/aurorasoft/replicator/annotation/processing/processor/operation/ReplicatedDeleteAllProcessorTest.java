//package by.aurorasoft.replicator.annotation.processing.processor.operation;
//
//import by.aurorasoft.replicator.util.AnnotationProcessUtil;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import javax.lang.model.element.VariableElement;
//import javax.lang.model.type.TypeMirror;
//import java.util.List;
//import java.util.Optional;
//
//import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteAllProcessor.REPLICATED_SERVICE_REQUIREMENT;
//import static by.aurorasoft.replicator.util.AnnotationProcessUtil.isContainRepository;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.mockStatic;
//
//public final class ReplicatedDeleteAllProcessorTest {
//    private final ReplicatedDeleteAllProcessor processor = new ReplicatedDeleteAllProcessor();
//
//    @Test
//    public void replicatedServiceShouldBeValid() {
//        try (MockedStatic<AnnotationProcessUtil> mockedAnnotationProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
//            TypeMirror givenMirror = mock(TypeMirror.class);
//            //noinspection ResultOfMethodCallIgnored
//            mockedAnnotationProcessUtil.when(() -> isContainRepository(givenMirror)).thenReturn(true);
//
//            boolean actual = processor.isValidReplicatedService(givenMirror);
//            assertTrue(actual);
//        }
//    }
//
//    @Test
//    public void replicatedServiceShouldNotBeValid() {
//        try (MockedStatic<AnnotationProcessUtil> mockedAnnotationProcessUtil = mockStatic(AnnotationProcessUtil.class)) {
//            TypeMirror givenMirror = mock(TypeMirror.class);
//            //noinspection ResultOfMethodCallIgnored
//            mockedAnnotationProcessUtil.when(() -> isContainRepository(givenMirror)).thenReturn(false);
//
//            boolean actual = processor.isValidReplicatedService(givenMirror);
//            assertFalse(actual);
//        }
//    }
//
//    @Test
//    public void returnTypeShouldBeValid() {
//        TypeMirror givenMirror = mock(TypeMirror.class);
//
//        boolean actual = processor.isValidReturnType(givenMirror);
//        assertTrue(actual);
//    }
//
//    @Test
//    public void parametersShouldBeValid() {
//        var givenElements = List.of(mock(VariableElement.class), mock(VariableElement.class));
//
//        boolean actual = processor.isValidParameters(givenElements);
//        assertTrue(actual);
//    }
//
//    @Test
//    public void replicatedServiceRequirementShouldBeGot() {
//        Optional<String> optionalActual = processor.getReplicatedServiceRequirement();
//        assertTrue(optionalActual.isPresent());
//        String actual = optionalActual.get();
//        assertSame(REPLICATED_SERVICE_REQUIREMENT, actual);
//    }
//
//    @Test
//    public void returnTypeRequirementShouldNotBeGot() {
//        Optional<String> optionalActual = processor.getReturnTypeRequirement();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void parametersRequirementShouldNotBeGot() {
//        Optional<String> optionalActual = processor.getParametersRequirement();
//        assertTrue(optionalActual.isEmpty());
//    }
//}
