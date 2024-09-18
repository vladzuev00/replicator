//package by.aurorasoft.replicator.annotation.processing.processor.operation;
//
//import by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import javax.lang.model.element.VariableElement;
//import javax.lang.model.type.TypeMirror;
//
//import java.util.List;
//import java.util.Optional;
//
//import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedDeleteAllProcessor.REPLICATED_SERVICE_REQUIREMENT;
//import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isContainRepository;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.mockStatic;
//
//public final class ReplicatedDeleteAllProcessorTest {
//    private final ReplicatedDeleteAllProcessor processor = new ReplicatedDeleteAllProcessor();
//
//    @Test
//    public void replicatedServiceShouldBeValid() {
//        try (MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
//            TypeMirror givenMirror = mock(TypeMirror.class);
//            mockedMirrorUtil.when(() -> isContainRepository(same(givenMirror), any())).thenReturn(true);
//
//            assertTrue(processor.isValidReplicatedService(givenMirror));
//        }
//    }
//
//    @Test
//    public void replicatedServiceShouldNotBeValid() {
//        try (MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
//            TypeMirror givenMirror = mock(TypeMirror.class);
//            mockedMirrorUtil.when(() -> isContainRepository(same(givenMirror), any())).thenReturn(false);
//
//            assertFalse(processor.isValidReplicatedService(givenMirror));
//        }
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
//        var givenElements = List.of(mock(VariableElement.class), mock(VariableElement.class));
//
//        assertTrue(processor.isValidParameters(givenElements));
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
