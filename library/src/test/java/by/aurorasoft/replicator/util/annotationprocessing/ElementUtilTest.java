package by.aurorasoft.replicator.util.annotationprocessing;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isIterable;
import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isJpaRepository;
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
}
