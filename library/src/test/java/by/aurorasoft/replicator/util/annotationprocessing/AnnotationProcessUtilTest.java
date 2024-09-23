package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.util.NameImpl;
import org.checkerframework.javacutil.TypesUtils;
import org.mockito.MockedStatic;
import org.testng.annotations.Test;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.*;
import static by.aurorasoft.replicator.util.annotationprocessing.ExecutableElementUtil.ID_GETTER_NAME;
import static java.util.Collections.emptyList;
import static javax.lang.model.element.ElementKind.PACKAGE;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import static javax.lang.model.type.TypeKind.*;
import static org.checkerframework.javacutil.TypesUtils.isErasedSubtype;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

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

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void mirrorShouldContainIdGetter() {
        TypeMirror givenMirror = mock(TypeMirror.class);
        Types givenTypeUtil = mock(Types.class);

        when(givenMirror.getKind()).thenReturn(DECLARED);

        Element givenElement = mock(Element.class);
        when(givenTypeUtil.asElement(same(givenMirror))).thenReturn(givenElement);

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

        assertTrue(isContainIdGetter(givenMirror, givenTypeUtil));
    }

    @Test
    public void mirrorShouldNotContainIdGetterBecauseOfItIsVoid() {
        TypeMirror givenMirror = mock(TypeMirror.class);
        Types givenTypeUtil = mock(Types.class);

        when(givenMirror.getKind()).thenReturn(VOID);

        assertFalse(isContainIdGetter(givenMirror, givenTypeUtil));
    }

    @Test
    public void mirrorShouldNotContainIdGetterBecauseOfItIsPrimitive() {
        TypeMirror givenMirror = mock(TypeMirror.class);
        Types givenTypeUtil = mock(Types.class);

        when(givenMirror.getKind()).thenReturn(DOUBLE);

        assertFalse(isContainIdGetter(givenMirror, givenTypeUtil));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void mirrorShouldNotContainIdGetter() {
        TypeMirror givenMirror = mock(TypeMirror.class);
        Types givenTypeUtil = mock(Types.class);

        when(givenMirror.getKind()).thenReturn(DECLARED);

        Element givenElement = mock(Element.class);
        when(givenTypeUtil.asElement(same(givenMirror))).thenReturn(givenElement);

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

        assertFalse(isContainIdGetter(givenMirror, givenTypeUtil));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void typeParameterElementShouldContainIdGetter() {
        TypeParameterElement givenElement = mock(TypeParameterElement.class);

        Element givenGenericElement = mock(Element.class);
        when(givenElement.getGenericElement()).thenReturn(givenGenericElement);

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
        when(givenGenericElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

        assertTrue(isContainIdGetter(givenElement));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void typeParameterElementShouldNotContainIdGetter() {
        TypeParameterElement givenElement = mock(TypeParameterElement.class);

        Element givenGenericElement = mock(Element.class);
        when(givenElement.getGenericElement()).thenReturn(givenGenericElement);

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
        when(givenGenericElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

        assertFalse(isContainIdGetter(givenElement));
    }

    @Test
    public void elementShouldBeIterable() {
        try (MockedStatic<TypesUtils> mockedTypeUtil = mockStatic(TypesUtils.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenElementMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenElementMirror);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(ITERABLE_TYPE_NAME))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtil.when(
                    () -> isErasedSubtype(
                            same(givenElementMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isIterable(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void elementShouldNotBeIterable() {
        try (MockedStatic<TypesUtils> mockedTypeUtil = mockStatic(TypesUtils.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenElementMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenElementMirror);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(ITERABLE_TYPE_NAME))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtil.when(
                    () -> isErasedSubtype(
                            same(givenElementMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isIterable(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void elementShouldBeJpaRepository() {
        try (MockedStatic<TypesUtils> mockedTypeUtil = mockStatic(TypesUtils.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenElementMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenElementMirror);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(JPA_REPOSITORY_TYPE_NAME))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtil.when(
                    () -> isErasedSubtype(
                            same(givenElementMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isJpaRepository(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void elementShouldNotBeJpaRepository() {
        try (MockedStatic<TypesUtils> mockedTypeUtil = mockStatic(TypesUtils.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenElementMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenElementMirror);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(JPA_REPOSITORY_TYPE_NAME))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtil.when(
                    () -> isErasedSubtype(
                            same(givenElementMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isJpaRepository(givenElement, givenElementUtil, givenTypeUtil));
        }
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
