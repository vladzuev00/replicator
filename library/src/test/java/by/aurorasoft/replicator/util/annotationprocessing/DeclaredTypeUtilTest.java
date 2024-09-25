package by.aurorasoft.replicator.util.annotationprocessing;

import org.junit.jupiter.api.Test;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static by.aurorasoft.replicator.util.annotationprocessing.DeclaredTypeUtil.getFirstTypeArgument;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class DeclaredTypeUtilTest {

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void firstTypeArgumentShouldBeGot() {
        DeclaredType givenDeclaredType = mock(DeclaredType.class);

        TypeMirror firstGivenArgument = mock(TypeMirror.class);
        List givenArguments = List.of(firstGivenArgument, mock(TypeMirror.class), mock(TypeMirror.class));
        when(givenDeclaredType.getTypeArguments()).thenReturn(givenArguments);

        TypeMirror actual = getFirstTypeArgument(givenDeclaredType);
        assertSame(firstGivenArgument, actual);
    }

    @Test
    public void firstTypeArgumentShouldNotBeGot() {
        DeclaredType givenDeclaredType = mock(DeclaredType.class);

        when(givenDeclaredType.getTypeArguments()).thenReturn(emptyList());

        assertThrows(IllegalArgumentException.class, () -> getFirstTypeArgument(givenDeclaredType));
    }
}
