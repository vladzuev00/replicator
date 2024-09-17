package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.ElementUtil.*;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class ElementUtilTest {

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
    public void elementShouldBeIterable() {
        try (MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            Element givenElement = mock(Element.class);
            ProcessingEnvironment givenEnvironment = mock(ProcessingEnvironment.class);

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);
            mockedMirrorUtil.when(() -> TypeMirrorUtil.isIterable(same(givenMirror), same(givenEnvironment)))
                    .thenReturn(true);

            assertTrue(isIterable(givenElement, givenEnvironment));
        }
    }

    @Test
    public void elementShouldNotBeIterable() {
        try (MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            Element givenElement = mock(Element.class);
            ProcessingEnvironment givenEnvironment = mock(ProcessingEnvironment.class);

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);
            mockedMirrorUtil.when(() -> TypeMirrorUtil.isIterable(same(givenMirror), same(givenEnvironment)))
                    .thenReturn(false);

            assertFalse(isIterable(givenElement, givenEnvironment));
        }
    }

    @Test
    public void firstTypeArgumentShouldBeGot() {
        try (MockedStatic<TypeMirrorUtil> mockedMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            Element givenElement = mock(Element.class);

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);

            TypeMirror givenFirstTypeArgument = mock(TypeMirror.class);
            mockedMirrorUtil.when(() -> TypeMirrorUtil.getFirstTypeArgument(same(givenMirror)))
                    .thenReturn(givenFirstTypeArgument);

            TypeMirror actual = getFirstTypeArgument(givenElement);
            assertSame(givenFirstTypeArgument, actual);
        }
    }

    @Test
    public void elementShouldBeIdGetter() {
        Element givenElement = createElement(METHOD, GETTER_NAME_ID, NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE);

        assertTrue(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfKindIsNotMethod() {
        Element givenElement = createElement(CONSTRUCTOR, GETTER_NAME_ID, NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE);

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfElementIsNotPublic() {
        Element givenElement = createElement(METHOD, GETTER_NAME_ID, NATIVE, SYNCHRONIZED, PROTECTED, VOLATILE);

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    public void elementShouldNotBeIdGetterBecauseOfNotSuitableName() {
        Element givenElement = createElement(METHOD, "GetId", NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE);

        assertFalse(isIdGetter(givenElement));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void elementShouldContainIdGetter() {
        Element givenElement = mock(Element.class);

        List givenEnclosedElements = List.of(
                createElement(METHOD, "GetId", NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE),
                createElement(CONSTRUCTOR, GETTER_NAME_ID, NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE),
                createElement(METHOD, GETTER_NAME_ID, NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE),
                createElement(CLASS, "class", PUBLIC, STATIC)
        );
        when(givenElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

        assertTrue(isContainIdGetter(givenElement));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void elementShouldNotContainIdGetter() {
        Element givenElement = mock(Element.class);

        List givenEnclosedElements = List.of(
                createElement(METHOD, "GetId", NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE),
                createElement(CONSTRUCTOR, GETTER_NAME_ID, NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE),
                createElement(CLASS, "class", PUBLIC, STATIC)
        );
        when(givenElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

        assertFalse(isContainIdGetter(givenElement));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void typeParameterElementShouldContainIdGetter() {
        TypeParameterElement givenTypeParameterElement = mock(TypeParameterElement.class);

        Element givenGenericElement = mock(Element.class);
        when(givenTypeParameterElement.getGenericElement()).thenReturn(givenTypeParameterElement);

        List givenEnclosedElements = List.of(
                createElement(METHOD, "GetId", NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE),
                createElement(CONSTRUCTOR, GETTER_NAME_ID, NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE),
                createElement(METHOD, GETTER_NAME_ID, NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE),
                createElement(CLASS, "class", PUBLIC, STATIC)
        );
        when(givenGenericElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

        assertTrue(isContainIdGetter(givenGenericElement));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void typeParameterElementShouldNotContainIdGetter() {
        TypeParameterElement givenTypeParameterElement = mock(TypeParameterElement.class);

        Element givenGenericElement = mock(Element.class);
        when(givenTypeParameterElement.getGenericElement()).thenReturn(givenTypeParameterElement);

        List givenEnclosedElements = List.of(
                createElement(METHOD, "GetId", NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE),
                createElement(CONSTRUCTOR, GETTER_NAME_ID, NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE),
                createElement(CLASS, "class", PUBLIC, STATIC)
        );
        when(givenGenericElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

        assertFalse(isContainIdGetter(givenGenericElement));
    }

    private Element createElement(ElementKind kind, String name, Modifier... modifiers) {
        Element element = mock(Element.class);
        when(element.getKind()).thenReturn(kind);
        when(element.getSimpleName()).thenReturn(new NameImpl(name));
        when(element.getModifiers()).thenReturn(Set.of(modifiers));
        return element;
    }

    @RequiredArgsConstructor
    @SuppressWarnings("NullableProblems")
    private static final class NameImpl implements Name {
        private final String value;

        @Override
        public boolean contentEquals(CharSequence text) {
            return value.contentEquals(text);
        }

        @Override
        public int length() {
            return value.length();
        }

        @Override
        public char charAt(int index) {
            return value.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return value.subSequence(start, end);
        }
    }
}
