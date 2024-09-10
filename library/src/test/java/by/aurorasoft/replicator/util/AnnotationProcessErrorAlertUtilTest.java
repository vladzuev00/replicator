package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.annotation.operation.ReplicatedSave;
import by.aurorasoft.replicator.annotation.processing.error.AnnotationError;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static by.aurorasoft.replicator.util.AnnotationProcessErrorAlertUtil.alert;
import static javax.tools.Diagnostic.Kind.ERROR;
import static org.mockito.Mockito.*;

public final class AnnotationProcessErrorAlertUtilTest {

    @Test
    public void errorShouldBeAlerted() {
        Element givenElement = mock(Element.class);
        Class<? extends Annotation> givenAnnotation = ReplicatedSave.class;
        Set<String> givenRequirements = new LinkedHashSet<>(
                List.of(
                        "first requirement",
                        "second requirement",
                        "third requirement"
                )
        );
        AnnotationError givenError = new AnnotationError(givenElement, givenAnnotation, givenRequirements);
        ProcessingEnvironment givenEnvironment = mock(ProcessingEnvironment.class);

        Messager givenMessager = mock(Messager.class);
        when(givenEnvironment.getMessager()).thenReturn(givenMessager);

        alert(givenError, givenEnvironment);

        String expectedMessage = """
                Element annotated by @ReplicatedSave should match next requirements:
                	first requirement
                	second requirement
                	third requirement""";
        verify(givenMessager, times(1)).printMessage(same(ERROR), eq(expectedMessage), same(givenElement));
    }
}
