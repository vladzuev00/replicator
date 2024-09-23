package by.aurorasoft.replicator.util.annotationprocessing;

import org.checkerframework.javacutil.TypesUtils;
import org.mockito.MockedStatic;
import org.testng.annotations.Test;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isErasedSubtype;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isVoid;
import static javax.lang.model.type.TypeKind.DOUBLE;
import static javax.lang.model.type.TypeKind.VOID;
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
    public void mirrorShouldBeErasedSubtype() {
        try (MockedStatic<TypesUtils> mockedTypeUtils = mockStatic(TypesUtils.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);
            String givenSuperTypeName = "SuperType";
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(givenSuperTypeName))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtils.when(
                    () -> TypesUtils.isErasedSubtype(
                            same(givenMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isErasedSubtype(givenMirror, givenSuperTypeName, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void mirrorShouldNotBeErasedSubtype() {
        try (MockedStatic<TypesUtils> mockedTypeUtils = mockStatic(TypesUtils.class)) {
            TypeMirror givenMirror = mock(TypeMirror.class);
            String givenSuperTypeName = "SuperType";
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeElement givenSuperType = mock(TypeElement.class);
            when(givenElementUtil.getTypeElement(same(givenSuperTypeName))).thenReturn(givenSuperType);

            TypeMirror givenSuperTypeMirror = mock(TypeMirror.class);
            when(givenSuperType.asType()).thenReturn(givenSuperTypeMirror);

            mockedTypeUtils.when(
                    () -> TypesUtils.isErasedSubtype(
                            same(givenMirror),
                            same(givenSuperTypeMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isErasedSubtype(givenMirror, givenSuperTypeName, givenElementUtil, givenTypeUtil));
        }
    }
}
