package by.aurorasoft.replicator.util.annotationprocessing;

import org.checkerframework.javacutil.TypesUtils;
import org.mockito.MockedStatic;
import org.testng.annotations.Test;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.*;
import static javax.lang.model.type.TypeKind.DOUBLE;
import static javax.lang.model.type.TypeKind.VOID;
import static org.checkerframework.javacutil.TypesUtils.isErasedSubtype;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class TypeMirrorUtilTest {

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
    public void mirrorShouldBeList() {
        try (MockedStatic<TypesUtils> mockedTypeUtil = mockStatic(TypesUtils.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(LIST_TYPE_NAME))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtil.when(
                    () -> isErasedSubtype(
                            same(givenMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isList(givenMirror, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void mirrorShouldNotBeList() {
        try (MockedStatic<TypesUtils> mockedTypeUtil = mockStatic(TypesUtils.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(LIST_TYPE_NAME))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtil.when(
                    () -> isErasedSubtype(
                            same(givenMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isList(givenMirror, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void mirrorShouldBeIterable() {
        try (MockedStatic<TypesUtils> mockedTypeUtil = mockStatic(TypesUtils.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(ITERABLE_TYPE_NAME))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtil.when(
                    () -> isErasedSubtype(
                            same(givenMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isIterable(givenMirror, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void mirrorShouldNotBeIterable() {
        try (MockedStatic<TypesUtils> mockedTypeUtil = mockStatic(TypesUtils.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(ITERABLE_TYPE_NAME))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtil.when(
                    () -> isErasedSubtype(
                            same(givenMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isIterable(givenMirror, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void mirrorShouldBeJpaRepository() {
        try (MockedStatic<TypesUtils> mockedTypeUtil = mockStatic(TypesUtils.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(JPA_REPOSITORY_TYPE_NAME))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtil.when(
                    () -> isErasedSubtype(
                            same(givenMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isJpaRepository(givenMirror, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void mirrorShouldNotBeJpaRepository() {
        try (MockedStatic<TypesUtils> mockedTypeUtil = mockStatic(TypesUtils.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(JPA_REPOSITORY_TYPE_NAME))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtil.when(
                    () -> isErasedSubtype(
                            same(givenMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isJpaRepository(givenMirror, givenElementUtil, givenTypeUtil));
        }
    }
}
