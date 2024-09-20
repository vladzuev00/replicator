package by.aurorasoft.replicator.util.annotationprocessing;

import org.testng.annotations.Test;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.getAnnotatedElements;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class AnnotationProcessUtilTest {

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void annotatedElementsShouldBeGot() {
        TypeElement givenAnnotation = mock(TypeElement.class);
        RoundEnvironment givenEnvironment = mock(RoundEnvironment.class);
        Class<ExecutableElement> givenElementType = ExecutableElement.class;

        Element firstGivenElement = mock(ExecutableElement.class);
        Element secondGivenElement = mock(ExecutableElement.class);
        Element thirdGivenElement = mock(ExecutableElement.class);
        Set givenElements = new LinkedHashSet<>(List.of(firstGivenElement, secondGivenElement, thirdGivenElement));
        when(givenEnvironment.getElementsAnnotatedWith(same(givenAnnotation))).thenReturn(givenElements);

        Stream<ExecutableElement> actual = getAnnotatedElements(givenAnnotation, givenEnvironment, givenElementType);
        List<ExecutableElement> actualAsList = actual.toList();
        List<Element> expectedAsList = List.of(firstGivenElement, secondGivenElement, thirdGivenElement);
        assertEquals(expectedAsList, actualAsList);
    }
}
