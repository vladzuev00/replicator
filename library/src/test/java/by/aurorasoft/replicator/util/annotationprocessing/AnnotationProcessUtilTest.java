package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import org.testng.annotations.Test;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.*;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void elementShouldBePublic() {
        Element givenElement = mock(Element.class);

        Set<Modifier> givenModifiers = Set.of(NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE);
        when(givenElement.getModifiers()).thenReturn(givenModifiers);

        assertTrue(isPublic(givenElement));
    }

    @Test
    public void elementShouldNotBePublic() {
        Element givenElement = mock(Element.class);

        Set<Modifier> givenModifiers = Set.of(NATIVE, SYNCHRONIZED, PRIVATE, VOLATILE);
        when(givenElement.getModifiers()).thenReturn(givenModifiers);

        assertFalse(isPublic(givenElement));
    }

    @Test
    public void elementShouldBeReplicatedService() {
        Element givenElement = mock(Element.class);

        when(givenElement.getAnnotation(same(ReplicatedService.class))).thenReturn(mock(ReplicatedService.class));

        assertTrue(isReplicatedService(givenElement));
    }

    @Test
    public void elementShouldNotBeReplicatedService() {
        Element givenElement = mock(Element.class);

        when(givenElement.getAnnotation(same(ReplicatedService.class))).thenReturn(null);

        assertFalse(isReplicatedService(givenElement));
    }

    @Test
    public void elementShouldBeClass() {
        Element givenElement = mock(Element.class);

        when(givenElement.getKind()).thenReturn(CLASS);

        assertTrue(isClass(givenElement));
    }

    @Test
    public void elementShouldNotBeClass() {
        Element givenElement = mock(Element.class);

        when(givenElement.getKind()).thenReturn(INTERFACE);

        assertFalse(isClass(givenElement));
    }

    @Test
    public void elementShouldBePackage() {
        Element givenElement = mock(Element.class);

        when(givenElement.getKind()).thenReturn(PACKAGE);

        assertTrue(isPackage(givenElement));
    }

    @Test
    public void elementShouldNotBePackage() {
        Element givenElement = mock(Element.class);

        when(givenElement.getKind()).thenReturn(CLASS);

        assertFalse(isPackage(givenElement));
    }

    @Test
    public void enclosingClassShouldBeGot() {
        ExecutableElement givenElement = mock(ExecutableElement.class);

        Element firstGivenEnclosingElement = createEnclosingElement(
                givenElement,
                INTERFACE,
                TypeElement.class
        );
        Element secondGivenEnclosingElement = createEnclosingElement(
                firstGivenEnclosingElement,
                INTERFACE,
                TypeElement.class
        );
        Element thirdGivenEnclosingElement = createEnclosingElement(
                secondGivenEnclosingElement,
                CLASS,
                TypeElement.class
        );
        createEnclosingElement(
                thirdGivenEnclosingElement,
                PACKAGE,
                PackageElement.class
        );

        TypeElement actual = getEnclosingClass(givenElement);
        assertSame(thirdGivenEnclosingElement, actual);
    }

    @Test
    public void enclosingClassShouldNotBeGot() {
        ExecutableElement givenElement = mock(ExecutableElement.class);

        Element firstGivenEnclosingElement = createEnclosingElement(
                givenElement,
                INTERFACE,
                TypeElement.class
        );
        Element secondGivenEnclosingElement = createEnclosingElement(
                firstGivenEnclosingElement,
                INTERFACE,
                TypeElement.class
        );
        Element thirdGivenEnclosingElement = createEnclosingElement(
                secondGivenEnclosingElement,
                INTERFACE,
                TypeElement.class
        );
        createEnclosingElement(
                thirdGivenEnclosingElement,
                PACKAGE,
                PackageElement.class
        );

        assertThrows(NoSuchElementException.class, () -> getEnclosingClass(givenElement));
    }

    private Element createEnclosingElement(Element enclosedElement, ElementKind kind, Class<? extends Element> type) {
        Element element = mock(type);
        when(enclosedElement.getEnclosingElement()).thenReturn(element);
        when(element.getKind()).thenReturn(kind);
        return element;
    }
}
