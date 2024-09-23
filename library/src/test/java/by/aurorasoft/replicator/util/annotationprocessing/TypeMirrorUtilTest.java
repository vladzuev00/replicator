package by.aurorasoft.replicator.util.annotationprocessing;

import org.testng.annotations.Test;

import javax.lang.model.type.TypeMirror;

import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isVoid;
import static javax.lang.model.type.TypeKind.DOUBLE;
import static javax.lang.model.type.TypeKind.VOID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}
