package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.util.NameImpl;
import org.testng.annotations.Test;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Set;

import static by.aurorasoft.replicator.util.annotationprocessing.ExecutableElementUtil.ID_GETTER_NAME;
import static by.aurorasoft.replicator.util.annotationprocessing.ExecutableElementUtil.isIdGetter;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ExecutableElement createElement(ElementKind kind, Set<Modifier> modifiers, String name, List parameters) {
        ExecutableElement element = mock(ExecutableElement.class);
        when(element.getKind()).thenReturn(kind);
        when(element.getModifiers()).thenReturn(modifiers);
        when(element.getSimpleName()).thenReturn(new NameImpl(name));
        when(element.getParameters()).thenReturn(parameters);
        return element;
    }
}
