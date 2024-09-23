package by.aurorasoft.replicator.util.annotationprocessing;

import org.junit.jupiter.api.Test;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static by.aurorasoft.replicator.util.annotationprocessing.TypeElementUtil.getAnnotatedElements;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class TypeElementUtilTest {

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

        var actual = getAnnotatedElements(givenAnnotation, givenEnvironment, givenElementType).toList();
        List<Element> expected = List.of(firstGivenElement, secondGivenElement, thirdGivenElement);
        assertEquals(expected, actual);
    }
}
