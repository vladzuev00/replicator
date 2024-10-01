//package by.aurorasoft.replicator.annotation.processing.processor.operation;
//
//import by.aurorasoft.replicator.util.annotationprocessing.DeclaredTypeUtil;
//import by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil;
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
//import javax.lang.model.type.DeclaredType;
//import javax.lang.model.type.TypeMirror;
//import javax.lang.model.util.Elements;
//import javax.lang.model.util.Types;
//import java.util.List;
//import java.util.Optional;
//
//import static by.aurorasoft.replicator.annotation.processing.processor.operation.ReplicatedSaveAllProcessor.RETURN_TYPE_REQUIREMENT;
//import static by.aurorasoft.replicator.util.annotationprocessing.DeclaredTypeUtil.getFirstTypeArgument;
//import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isContainIdGetter;
//import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isList;
//import static java.util.Collections.emptyList;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public final class ReplicatedSaveAllProcessorTest {
//    private final ReplicatedSaveAllProcessor processor = new ReplicatedSaveAllProcessor();
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
//        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class);
//             MockedStatic<DeclaredTypeUtil> mockedDeclaredTypeUtil = mockStatic(DeclaredTypeUtil.class)) {
//            DeclaredType givenDeclaredType = mock(DeclaredType.class);
//
//            Elements givenElementUtil = mock(Elements.class);
//            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);
//
//            Types givenTypeUtil = mock(Types.class);
//            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);
//
//            mockedTypeMirrorUtil.when(
//                    () -> isList(
//                            same(givenDeclaredType),
//                            same(givenElementUtil),
//                            same(givenTypeUtil)
//                    )
//            ).thenReturn(true);
//
//            TypeMirror givenFirstTypeArgument = mock(TypeMirror.class);
//            mockedDeclaredTypeUtil.when(() -> getFirstTypeArgument(same(givenDeclaredType)))
//                    .thenReturn(givenFirstTypeArgument);
//            mockedTypeMirrorUtil.when(() -> isContainIdGetter(same(givenFirstTypeArgument), same(givenTypeUtil)))
//                    .thenReturn(true);
//
//            assertTrue(processor.isValidReturnType(givenDeclaredType));
//        }
//    }
//
//    @Test
//    public void returnTypeShouldNotBeValidBecauseOfNotList() {
//        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class);
//             MockedStatic<DeclaredTypeUtil> mockedDeclaredTypeUtil = mockStatic(DeclaredTypeUtil.class)) {
//            TypeMirror givenMirror = mock(TypeMirror.class);
//
//            Elements givenElementUtil = mock(Elements.class);
//            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);
//
//            Types givenTypeUtil = mock(Types.class);
//            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);
//
//            mockedTypeMirrorUtil.when(() -> isList(same(givenMirror), same(givenElementUtil), same(givenTypeUtil)))
//                    .thenReturn(false);
//
//            assertFalse(processor.isValidReturnType(givenMirror));
//
//            mockedTypeMirrorUtil.verify(() -> isContainIdGetter(any(TypeMirror.class), any(Types.class)), times(0));
//            mockedDeclaredTypeUtil.verifyNoInteractions();
//        }
//    }
//
//    @Test
//    public void returnTypeShouldNotBeValidBecauseOfListTypeArgumentNotContainId() {
//        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class);
//             MockedStatic<DeclaredTypeUtil> mockedDeclaredTypeUtil = mockStatic(DeclaredTypeUtil.class)) {
//            DeclaredType givenDeclaredType = mock(DeclaredType.class);
//
//            Elements givenElementUtil = mock(Elements.class);
//            when(mockedEnvironment.getElementUtils()).thenReturn(givenElementUtil);
//
//            Types givenTypeUtil = mock(Types.class);
//            when(mockedEnvironment.getTypeUtils()).thenReturn(givenTypeUtil);
//
//            mockedTypeMirrorUtil.when(
//                    () -> isList(
//                            same(givenDeclaredType),
//                            same(givenElementUtil),
//                            same(givenTypeUtil)
//                    )
//            ).thenReturn(true);
//
//            TypeMirror givenFirstTypeArgument = mock(TypeMirror.class);
//            mockedDeclaredTypeUtil.when(() -> getFirstTypeArgument(same(givenDeclaredType)))
//                    .thenReturn(givenFirstTypeArgument);
//            mockedTypeMirrorUtil.when(() -> isContainIdGetter(same(givenFirstTypeArgument), same(givenTypeUtil)))
//                    .thenReturn(false);
//
//            assertFalse(processor.isValidReturnType(givenDeclaredType));
//        }
//    }
//
//    @Test
//    public void parametersShouldBeValid() {
//        List<VariableElement> givenElements = emptyList();
//
//        assertTrue(processor.isValidParameters(givenElements));
//    }
//
//    @Test
//    public void replicatedServiceRequirementShouldNotBeGot() {
//        Optional<String> optionalActual = processor.getReplicatedServiceRequirement();
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void returnTypeRequirementShouldBeGot() {
//        Optional<String> optionalActual = processor.getReturnTypeRequirement();
//        assertTrue(optionalActual.isPresent());
//        String actual = optionalActual.get();
//        assertSame(RETURN_TYPE_REQUIREMENT, actual);
//    }
//
//    @Test
//    public void parametersRequirementShouldNotBeGot() {
//        Optional<String> optionalActual = processor.getParametersRequirement();
//        assertTrue(optionalActual.isEmpty());
//    }
//}
