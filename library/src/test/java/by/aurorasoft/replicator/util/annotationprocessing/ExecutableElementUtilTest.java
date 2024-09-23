package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.util.NameImpl;
import org.testng.annotations.Test;

import javax.lang.model.element.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static by.aurorasoft.replicator.util.annotationprocessing.ExecutableElementUtil.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ExecutableElementUtilTest {

    @Test
    public void elementShouldBeIdGetter() {
        var givenElement = createElement(METHOD, Set.of(SYNCHRONIZED, VOLATILE, PUBLIC), ID_GETTER_NAME, emptyList());

        assertTrue(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfNotSuitableKind() {
        ExecutableElement givenElement = createElement(
                CONSTRUCTOR,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC),
                ID_GETTER_NAME,
                emptyList()
        );

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfNoPublicModifier() {
        ExecutableElement givenElement = createElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PROTECTED),
                ID_GETTER_NAME,
                emptyList()
        );

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfStaticModifier() {
        ExecutableElement givenElement = createElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC, STATIC),
                ID_GETTER_NAME,
                emptyList()
        );

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfNotSuitableName() {
        var givenElement = createElement(METHOD, Set.of(SYNCHRONIZED, VOLATILE, PUBLIC), "GetId", emptyList());

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfParametersIsNotEmpty() {
        ExecutableElement givenElement = createElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC),
                ID_GETTER_NAME,
                singletonList(mock(VariableElement.class))
        );

        assertFalse(isIdGetter(givenElement));
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ExecutableElement createElement(ElementKind kind, Set<Modifier> modifiers, String name, List parameters) {
        ExecutableElement element = mock(ExecutableElement.class);
        when(element.getKind()).thenReturn(kind);
        when(element.getModifiers()).thenReturn(modifiers);
        when(element.getSimpleName()).thenReturn(new NameImpl(name));
        when(element.getParameters()).thenReturn(parameters);
        return element;
    }

    private Element createEnclosingElement(Element enclosedElement, ElementKind kind, Class<? extends Element> type) {
        Element element = mock(type);
        when(enclosedElement.getEnclosingElement()).thenReturn(element);
        when(element.getKind()).thenReturn(kind);
        return element;
    }
}
