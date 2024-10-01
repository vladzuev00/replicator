package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Set;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.*;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public final class ElementUtilTest {

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
    public void firstTypeArgumentShouldBeGot() {
        try (MockedStatic<DeclaredTypeUtil> mockedDeclaredTypeUtil = mockStatic(DeclaredTypeUtil.class)) {
            Element givenElement = mock(Element.class);

            DeclaredType givenDeclaredType = mock(DeclaredType.class);
            when(givenElement.asType()).thenReturn(givenDeclaredType);

            TypeMirror givenFirstTypeArgument = mock(TypeMirror.class);
            mockedDeclaredTypeUtil
                    .when(() -> DeclaredTypeUtil.getFirstTypeArgument(same(givenDeclaredType)))
                    .thenReturn(givenFirstTypeArgument);

            TypeMirror actual = getFirstTypeArgument(givenElement);
            assertSame(givenFirstTypeArgument, actual);
        }
    }
}
