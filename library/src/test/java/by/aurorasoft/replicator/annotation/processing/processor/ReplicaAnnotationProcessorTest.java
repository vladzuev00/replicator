package by.aurorasoft.replicator.annotation.processing.processor;

import by.aurorasoft.replicator.annotation.processing.error.AnnotationProcessError;
import by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessErrorAlertUtil;
import by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.annotation.processing.processor.ReplicaAnnotationProcessor.PUBLIC_MODIFIER_REQUIREMENT;
import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessErrorAlertUtil.alert;
import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.getAnnotatedElements;
import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.isPublic;
import static java.util.Arrays.stream;
import static javax.lang.model.SourceVersion.latestSupported;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith(MockitoExtension.class)
public final class ReplicaAnnotationProcessorTest {
    private final TestReplicaAnnotationProcessor processor = new TestReplicaAnnotationProcessor();

    @Mock
    private ProcessingEnvironment mockedEnvironment;

    @Captor
    private ArgumentCaptor<AnnotationProcessError> errorCaptor;

    @BeforeEach
    public void initializeProcessor() {
        processor.init(mockedEnvironment);
    }

    @Test
    public void annotationsShouldBeProcessedWithoutError() {
        try (var mockedProcessUtil = mockStatic(AnnotationProcessUtil.class);
             var mockedProcessErrorAlertUtil = mockStatic(AnnotationProcessErrorAlertUtil.class)) {
            TypeElement firstGivenAnnotation = mock(TypeElement.class);
            TypeElement secondGivenAnnotation = mock(TypeElement.class);
            Set<? extends TypeElement> givenAnnotations = Set.of(firstGivenAnnotation, secondGivenAnnotation);
            RoundEnvironment givenEnvironment = mock(RoundEnvironment.class);

            ExecutableElement firstGivenMethod = createMethod(true, true, mockedProcessUtil);
            ExecutableElement secondGivenMethod = createMethod(true, true, mockedProcessUtil);
            ExecutableElement thirdGivenMethod = createMethod(true, true, mockedProcessUtil);
            ExecutableElement fourthGivenMethod = createMethod(true, true, mockedProcessUtil);
            bindMethodsWithAnnotation(
                    firstGivenAnnotation,
                    givenEnvironment,
                    mockedProcessUtil,
                    firstGivenMethod,
                    secondGivenMethod,
                    thirdGivenMethod
            );
            bindMethodsWithAnnotation(
                    secondGivenAnnotation,
                    givenEnvironment,
                    mockedProcessUtil,
                    fourthGivenMethod
            );

            assertTrue(processor.process(givenAnnotations, givenEnvironment));

            mockedProcessErrorAlertUtil.verifyNoInteractions();
        }
    }

    @Test
    @MockitoSettings(strictness = LENIENT)
    public void annotationsShouldBeProcessedWithErrors() {
        try (var mockedProcessUtil = mockStatic(AnnotationProcessUtil.class);
             var mockedProcessErrorAlertUtil = mockStatic(AnnotationProcessErrorAlertUtil.class)) {
            TypeElement firstGivenAnnotation = mock(TypeElement.class);
            TypeElement secondGivenAnnotation = mock(TypeElement.class);
            var givenAnnotations = new LinkedHashSet<>(List.of(firstGivenAnnotation, secondGivenAnnotation));
            RoundEnvironment givenEnvironment = mock(RoundEnvironment.class);

            ExecutableElement firstGivenMethod = createMethod(true, true, mockedProcessUtil);
            ExecutableElement secondGivenMethod = createMethod(true, false, mockedProcessUtil);
            ExecutableElement thirdGivenMethod = createMethod(true, true, mockedProcessUtil);
            ExecutableElement fourthGivenMethod = createMethod(false, true, mockedProcessUtil);
            bindMethodsWithAnnotation(
                    firstGivenAnnotation,
                    givenEnvironment,
                    mockedProcessUtil,
                    firstGivenMethod,
                    secondGivenMethod,
                    thirdGivenMethod
            );
            bindMethodsWithAnnotation(
                    secondGivenAnnotation,
                    givenEnvironment,
                    mockedProcessUtil,
                    fourthGivenMethod
            );

            assertTrue(processor.process(givenAnnotations, givenEnvironment));

            mockedProcessErrorAlertUtil.verify(() -> alert(errorCaptor.capture(), any()), times(2));
            List<AnnotationProcessError> actualErrors = errorCaptor.getAllValues();
            Class<? extends Annotation> expectedAnnotation = TestAnnotation.class;
            Set<String> expectedRequirements = Set.of(
                    PUBLIC_MODIFIER_REQUIREMENT,
                    TestReplicaAnnotationProcessor.FIRST_REQUIREMENT,
                    TestReplicaAnnotationProcessor.SECOND_REQUIREMENT,
                    TestReplicaAnnotationProcessor.THIRD_REQUIREMENT
            );
            List<AnnotationProcessError> expectedErrors = List.of(
                    new AnnotationProcessError(secondGivenMethod, expectedAnnotation, expectedRequirements),
                    new AnnotationProcessError(fourthGivenMethod, expectedAnnotation, expectedRequirements)
            );
            assertEquals(expectedErrors, actualErrors);
        }
    }

    @Test
    public void supportedAnnotationTypesShouldBeGot() {
        Set<String> actual = processor.getSupportedAnnotationTypes();
        Set<String> expected = Set.of(TestAnnotation.class.getName());
        assertEquals(expected, actual);
    }

    @Test
    public void supportedSourceVersionShouldBeGot() {
        SourceVersion actual = processor.getSupportedSourceVersion();
        SourceVersion expected = latestSupported();
        assertSame(expected, actual);
    }

    @Test
    public void typeUtilShouldBeGot() {
        Types givenUtil = mock(Types.class);
        when(mockedEnvironment.getTypeUtils()).thenReturn(givenUtil);

        Types actual = processor.getTypeUtil();
        assertSame(givenUtil, actual);
    }

    @Test
    public void elementUtilShouldBeGot() {
        Elements givenUtil = mock(Elements.class);
        when(mockedEnvironment.getElementUtils()).thenReturn(givenUtil);

        Elements actual = processor.getElementUtil();
        assertSame(givenUtil, actual);
    }

    private ExecutableElement createMethod(boolean publicValue,
                                           boolean varArgs,
                                           MockedStatic<AnnotationProcessUtil> processUtil) {
        ExecutableElement method = mock(ExecutableElement.class);
        processUtil.when(() -> isPublic(same(method))).thenReturn(publicValue);
        when(method.isVarArgs()).thenReturn(varArgs);
        return method;
    }

    private void bindMethodsWithAnnotation(TypeElement annotation,
                                           RoundEnvironment environment,
                                           MockedStatic<AnnotationProcessUtil> processUtil,
                                           ExecutableElement... elements) {
        processUtil.when(() -> getAnnotatedElements(same(annotation), same(environment), same(ExecutableElement.class)))
                .thenReturn(stream(elements));
    }

    private @interface TestAnnotation {

    }

    private static final class TestReplicaAnnotationProcessor extends ReplicaAnnotationProcessor<ExecutableElement> {
        private static final String FIRST_REQUIREMENT = "first requirement";
        private static final String SECOND_REQUIREMENT = "second requirement";
        private static final String THIRD_REQUIREMENT = "third requirement";
        private static final List<String> REQUIREMENTS = List.of(
                FIRST_REQUIREMENT,
                SECOND_REQUIREMENT,
                THIRD_REQUIREMENT
        );

        public TestReplicaAnnotationProcessor() {
            super(TestAnnotation.class, ExecutableElement.class);
        }

        @Override
        protected boolean isValidPublicElement(ExecutableElement element) {
            return element.isVarArgs();
        }

        @Override
        protected Stream<String> getRequirementsInternal() {
            return REQUIREMENTS.stream();
        }
    }
}
