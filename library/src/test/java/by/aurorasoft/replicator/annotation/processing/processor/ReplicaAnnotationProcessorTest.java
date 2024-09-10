package by.aurorasoft.replicator.annotation.processing.processor;

import by.aurorasoft.replicator.annotation.processing.error.AnnotationProcessError;
import by.aurorasoft.replicator.util.AnnotationProcessErrorAlertUtil;
import by.aurorasoft.replicator.util.AnnotationProcessUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.AnnotationProcessErrorAlertUtil.alert;
import static by.aurorasoft.replicator.util.AnnotationProcessUtil.getAnnotatedElements;
import static by.aurorasoft.replicator.util.AnnotationProcessUtil.isPublic;
import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ReplicaAnnotationProcessorTest {
    private static final String FIELD_NAME_PROCESSOR_PROCESSING_ENVIRONMENT = "processingEnv";

    private final TestReplicaAnnotationProcessor processor = new TestReplicaAnnotationProcessor();

    @Captor
    private ArgumentCaptor<AnnotationProcessError> errorCaptor;

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

            boolean actual = processor.process(givenAnnotations, givenEnvironment);
            assertTrue(actual);

            mockedProcessErrorAlertUtil.verifyNoInteractions();
        }
    }

    @Test
    public void annotationsShouldBeProcessedWithErrors() {
        try (var mockedProcessUtil = mockStatic(AnnotationProcessUtil.class);
             var mockedProcessErrorAlertUtil = mockStatic(AnnotationProcessErrorAlertUtil.class)) {
            TypeElement firstGivenAnnotation = mock(TypeElement.class);
            TypeElement secondGivenAnnotation = mock(TypeElement.class);
            Set<? extends TypeElement> givenAnnotations = Set.of(firstGivenAnnotation, secondGivenAnnotation);
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

            boolean actual = processor.process(givenAnnotations, givenEnvironment);
            assertTrue(actual);

            ProcessingEnvironment expectedProcessingEnvironment = getProcessingEnvironment();
            mockedProcessErrorAlertUtil.verify(
                    () -> alert(errorCaptor.capture(), same(expectedProcessingEnvironment)),
                    times(2)
            );
            Set<AnnotationProcessError> actualErrors = new HashSet<>(errorCaptor.getAllValues());
            Set<AnnotationProcessError> expectedErrors = Set.of(
                    new AnnotationProcessError(secondGivenMethod, TestAnnotation.class, Set.of()),
                    new AnnotationProcessError(secondGivenMethod, TestAnnotation.class, Set.of(""))
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
        throw new UnsupportedOperationException();
    }

    private ExecutableElement createMethod(boolean publicValue,
                                           boolean varArgs,
                                           MockedStatic<AnnotationProcessUtil> processUtil) {
        ExecutableElement method = mock(ExecutableElement.class);
        //noinspection ResultOfMethodCallIgnored
        processUtil.when(() -> isPublic(same(method))).thenReturn(publicValue);
        when(method.isVarArgs()).thenReturn(varArgs);
        return method;
    }

    private void bindMethodsWithAnnotation(TypeElement annotation,
                                           RoundEnvironment environment,
                                           MockedStatic<AnnotationProcessUtil> processUtil,
                                           ExecutableElement... elements) {
        processUtil.when(
                () -> getAnnotatedElements(
                        same(annotation),
                        same(environment),
                        same(ExecutableElement.class)
                )
        ).thenReturn(stream(elements));
    }

    private ProcessingEnvironment getProcessingEnvironment() {
        return getFieldValue(processor, FIELD_NAME_PROCESSOR_PROCESSING_ENVIRONMENT, ProcessingEnvironment.class);
    }

    private @interface TestAnnotation {

    }

    private static final class TestReplicaAnnotationProcessor extends ReplicaAnnotationProcessor<ExecutableElement> {
        private static final List<String> REQUIREMENTS = List.of(
                "first requirement",
                "second requirement",
                "third requirement"
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
