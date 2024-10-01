package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.util.NameImpl;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isJpaRepository;
import static by.aurorasoft.replicator.util.annotationprocessing.VariableElementUtil.*;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.FIELD;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public final class VariableElementUtilTest {

    @Test
    public void elementShouldContainIdGetter() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);

            mockedTypeMirrorUtil.when(
                    () -> TypeMirrorUtil.isContainIdGetter(
                            same(givenMirror),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isContainIdGetter(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void elementShouldNotContainIdGetter() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeMirror givenMirror = mock(TypeMirror.class);
            when(givenElement.asType()).thenReturn(givenMirror);

            mockedTypeMirrorUtil.when(
                    () -> TypeMirrorUtil.isContainIdGetter(
                            same(givenMirror),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isContainIdGetter(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void elementShouldBeJpaRepositoryField() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            when(givenElement.getKind()).thenReturn(FIELD);
            when(givenElement.getSimpleName()).thenReturn(new NameImpl(JPA_REPOSITORY_FIELD_NAME));
            mockedElementUtil.when(
                    () -> isJpaRepository(
                            same(givenElement),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(true);

            assertTrue(isJpaRepositoryField(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    public void elementShouldNotBeJpaRepositoryFieldBecauseOfNotSuitableKind() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            when(givenElement.getKind()).thenReturn(CLASS);

            assertFalse(isJpaRepositoryField(givenElement, givenElementUtil, givenTypeUtil));

            mockedElementUtil.verifyNoInteractions();
        }
    }

    @Test
    public void elementShouldNotBeJpaRepositoryFieldBecauseOfNotSuitableName() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            when(givenElement.getKind()).thenReturn(FIELD);
            when(givenElement.getSimpleName()).thenReturn(new NameImpl("jpaRepository"));

            assertFalse(isJpaRepositoryField(givenElement, givenElementUtil, givenTypeUtil));

            mockedElementUtil.verifyNoInteractions();
        }
    }

    @Test
    public void elementShouldNotBeJpaRepositoryFieldBecauseOfNotSuitableType() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            VariableElement givenElement = mock(VariableElement.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            when(givenElement.getKind()).thenReturn(FIELD);
            when(givenElement.getSimpleName()).thenReturn(new NameImpl(JPA_REPOSITORY_FIELD_NAME));
            mockedElementUtil.when(
                    () -> isJpaRepository(
                            same(givenElement),
                            same(givenElementUtil),
                            same(givenTypeUtil)
                    )
            ).thenReturn(false);

            assertFalse(isJpaRepositoryField(givenElement, givenElementUtil, givenTypeUtil));
        }
    }
}
