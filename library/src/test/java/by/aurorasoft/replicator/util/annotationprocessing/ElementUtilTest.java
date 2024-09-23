package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.util.NameImpl;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.*;
import static by.aurorasoft.replicator.util.annotationprocessing.ExecutableElementUtil.isIdGetter;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.PARAMETER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class ElementUtilTest {

    @Test
    public void elementShouldBeIterable() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);

            mockedTypeMirrorUtil.when(
                    () -> TypeMirrorUtil.isIterable(
                            same(givenMirror),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isIterable(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void elementShouldNotBeIterable() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);

            mockedTypeMirrorUtil.when(
                    () -> TypeMirrorUtil.isIterable(
                            same(givenMirror),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isIterable(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void elementShouldBeJpaRepository() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);

            mockedTypeMirrorUtil.when(
                    () -> TypeMirrorUtil.isJpaRepository(
                            same(givenMirror),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isJpaRepository(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void elementShouldNotBeJpaRepository() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);

            mockedTypeMirrorUtil.when(
                    () -> TypeMirrorUtil.isJpaRepository(
                            same(givenMirror),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isJpaRepository(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void elementShouldContainIdGetter() {
        try (MockedStatic<ExecutableElementUtil> mockedExecutableElementUtil = mockStatic(ExecutableElementUtil.class)) {
            Element givenElement = mock(Element.class);

            Element firstGivenEnclosedElement = mock(Element.class);
            Element secondGivenEnclosedElement = mock(Element.class);
            ExecutableElement thirdGivenEnclosedElement = mock(ExecutableElement.class);
            ExecutableElement fourthGivenEnclosedElement = mock(ExecutableElement.class);
            Element fifthGivenEnclosedElement = mock(Element.class);
            ExecutableElement sixthGivenEnclosedElement = mock(ExecutableElement.class);
            List givenEnclosedElements = List.of(
                    firstGivenEnclosedElement,
                    secondGivenEnclosedElement,
                    thirdGivenEnclosedElement,
                    fourthGivenEnclosedElement,
                    fifthGivenEnclosedElement,
                    sixthGivenEnclosedElement
            );
            when(givenElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

            mockedExecutableElementUtil.when(() -> isIdGetter(same(thirdGivenEnclosedElement))).thenReturn(false);
            mockedExecutableElementUtil.when(() -> isIdGetter(same(fourthGivenEnclosedElement))).thenReturn(true);

            assertTrue(isContainIdGetter(givenElement));

            mockedExecutableElementUtil.verify(() -> isIdGetter(same(sixthGivenEnclosedElement)), times(0));
        }
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void elementShouldNotContainIdGetter() {
        try (MockedStatic<ExecutableElementUtil> mockedExecutableElementUtil = mockStatic(ExecutableElementUtil.class)) {
            Element givenElement = mock(Element.class);

            Element firstGivenEnclosedElement = mock(Element.class);
            Element secondGivenEnclosedElement = mock(Element.class);
            ExecutableElement thirdGivenEnclosedElement = mock(ExecutableElement.class);
            ExecutableElement fourthGivenEnclosedElement = mock(ExecutableElement.class);
            Element fifthGivenEnclosedElement = mock(Element.class);
            ExecutableElement sixthGivenEnclosedElement = mock(ExecutableElement.class);
            List givenEnclosedElements = List.of(
                    firstGivenEnclosedElement,
                    secondGivenEnclosedElement,
                    thirdGivenEnclosedElement,
                    fourthGivenEnclosedElement,
                    fifthGivenEnclosedElement,
                    sixthGivenEnclosedElement
            );
            when(givenElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

            mockedExecutableElementUtil.when(() -> isIdGetter(same(thirdGivenEnclosedElement))).thenReturn(false);
            mockedExecutableElementUtil.when(() -> isIdGetter(same(fourthGivenEnclosedElement))).thenReturn(false);
            mockedExecutableElementUtil.when(() -> isIdGetter(same(sixthGivenEnclosedElement))).thenReturn(false);

            assertFalse(isContainIdGetter(givenElement));
        }
    }

    @Test
    public void elementShouldBeJpaRepositoryField() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            when(givenElement.getKind()).thenReturn(FIELD);
            when(givenElement.getSimpleName()).thenReturn(new NameImpl(JPA_REPOSITORY_FIELD_NAME));

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);
            mockedTypeMirrorUtil.when(
                    () -> TypeMirrorUtil.isJpaRepository(
                            same(givenMirror),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isJpaRepositoryField(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void elementShouldNotBeJpaRepositoryFieldBecauseOfNotSuitableKind() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            when(givenElement.getKind()).thenReturn(PARAMETER);

            assertFalse(isJpaRepositoryField(givenElement, givenElementUtil, givenTypeUtil));

            mockedTypeMirrorUtil.verifyNoInteractions();
        }
    }

    @Test
    public void elementShouldNotBeJpaRepositoryFieldBecauseOfNotSuitableName() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            when(givenElement.getKind()).thenReturn(FIELD);
            when(givenElement.getSimpleName()).thenReturn(new NameImpl("jpaRepository"));

            assertFalse(isJpaRepositoryField(givenElement, givenElementUtil, givenTypeUtil));

            mockedTypeMirrorUtil.verifyNoInteractions();
        }
    }

    @Test
    public void elementShouldNotBeJpaRepositoryFieldBecauseOfNotSuitableType() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            Element givenElement = mock(Element.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            when(givenElement.getKind()).thenReturn(FIELD);
            when(givenElement.getSimpleName()).thenReturn(new NameImpl(JPA_REPOSITORY_FIELD_NAME));

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);
            mockedTypeMirrorUtil.when(
                    () -> TypeMirrorUtil.isJpaRepository(
                            same(givenMirror),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isJpaRepositoryField(givenElement, givenElementUtil, givenTypeUtil));
        }
    }
}
