package by.aurorasoft.replicator.util.annotationprocessing;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import static by.aurorasoft.replicator.util.annotationprocessing.VariableElementUtil.isContainIdGetter;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class VariableElementUtilTest {

    @Test
    public void elementShouldContainIdGetter() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);

            mockedTypeMirrorUtil.when(
                    () -> TypeMirrorUtil.isContainIdGetter(
                            same(givenMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isContainIdGetter(givenElement, givenTypeUtil));
        }
    }

    @Test
    public void elementShouldNotContainIdGetter() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);

            mockedTypeMirrorUtil.when(
                    () -> TypeMirrorUtil.isContainIdGetter(
                            same(givenMirror),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isContainIdGetter(givenElement, givenTypeUtil));
        }
    }
}
