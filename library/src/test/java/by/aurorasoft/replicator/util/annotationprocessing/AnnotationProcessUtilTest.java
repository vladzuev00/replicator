package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.util.NameImpl;
import org.testng.annotations.Test;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import static javax.lang.model.type.TypeKind.DOUBLE;
import static javax.lang.model.type.TypeKind.VOID;
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

    @Test
    public void elementShouldBeIdGetter() {
        ExecutableElement givenElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC),
                ID_GETTER_NAME,
                emptyList()
        );

        assertTrue(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfNotSuitableKind() {
        ExecutableElement givenElement = createExecutableElement(
                CONSTRUCTOR,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC),
                ID_GETTER_NAME,
                emptyList()
        );

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfNoPublicModifier() {
        ExecutableElement givenElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PROTECTED),
                ID_GETTER_NAME,
                emptyList()
        );

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfStaticModifier() {
        ExecutableElement givenElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC, STATIC),
                ID_GETTER_NAME,
                emptyList()
        );

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfNotSuitableName() {
        ExecutableElement givenElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC),
                "GetId",
                emptyList()
        );

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfParametersIsNotEmpty() {
        ExecutableElement givenElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC),
                ID_GETTER_NAME,
                singletonList(mock(VariableElement.class))
        );

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    public void mirrorShouldBeVoid() {
        TypeMirror givenMirror = mock(TypeMirror.class);

        when(givenMirror.getKind()).thenReturn(VOID);

        assertTrue(isVoid(givenMirror));
    }

    @Test
    public void mirrorShouldNotBeVoid() {
        TypeMirror givenMirror = mock(TypeMirror.class);

        when(givenMirror.getKind()).thenReturn(DOUBLE);

        assertFalse(isVoid(givenMirror));
    }

    @Test
    public void mirrorShouldBePrimitive() {
        TypeMirror givenMirror = mock(TypeMirror.class);

        when(givenMirror.getKind()).thenReturn(DOUBLE);

        assertTrue(isPrimitive(givenMirror));
    }

    @Test
    public void mirrorShouldNotBePrimitive() {
        TypeMirror givenMirror = mock(TypeMirror.class);

        when(givenMirror.getKind()).thenReturn(VOID);

        assertFalse(isPrimitive(givenMirror));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void elementShouldContainIdGetter() {
        Element givenElement = mock(Element.class);

        Element firstGivenEnclosedElement = mock(Element.class);
        Element secondGivenEnclosedElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE),
                ID_GETTER_NAME,
                emptyList()
        );
        Element thirdGivenEnclosedElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC),
                ID_GETTER_NAME,
                emptyList()
        );
        Element fourthGivenEnclosedElement = mock(Element.class);
        List givenEnclosedElements = List.of(
                firstGivenEnclosedElement,
                secondGivenEnclosedElement,
                thirdGivenEnclosedElement,
                fourthGivenEnclosedElement
        );
        when(givenElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

        assertTrue(isContainIdGetter(givenElement));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void elementShouldNotContainIdGetter() {
        Element givenElement = mock(Element.class);

        Element firstGivenEnclosedElement = mock(Element.class);
        Element secondGivenEnclosedElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE),
                ID_GETTER_NAME,
                emptyList()
        );
        Element thirdGivenEnclosedElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC),
                "GetId",
                emptyList()
        );
        Element fourthGivenEnclosedElement = mock(Element.class);
        List givenEnclosedElements = List.of(
                firstGivenEnclosedElement,
                secondGivenEnclosedElement,
                thirdGivenEnclosedElement,
                fourthGivenEnclosedElement
        );
        when(givenElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

        assertFalse(isContainIdGetter(givenElement));
    }

    private Element createEnclosingElement(Element enclosedElement, ElementKind kind, Class<? extends Element> type) {
        Element element = mock(type);
        when(enclosedElement.getEnclosingElement()).thenReturn(element);
        when(element.getKind()).thenReturn(kind);
        return element;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ExecutableElement createExecutableElement(ElementKind kind,
                                                      Set<Modifier> modifiers,
                                                      String name,
                                                      List parameters) {
        ExecutableElement element = mock(ExecutableElement.class);
        when(element.getKind()).thenReturn(kind);
        when(element.getModifiers()).thenReturn(modifiers);
        when(element.getSimpleName()).thenReturn(new NameImpl(name));
        when(element.getParameters()).thenReturn(parameters);
        return element;
    }
}
