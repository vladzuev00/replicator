package by.aurorasoft.replicator.util;

import org.junit.jupiter.api.Test;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.NoSuchElementException;

import static by.aurorasoft.replicator.util.DeclaredTypeUtil.getFirstTypeArgument;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class DeclaredTypeUtilTest {

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void firstTypeArgumentShouldBeGot() {
        DeclaredType givenType = mock(DeclaredType.class);

        TypeMirror givenFirstTypeArgument = mock(TypeMirror.class);
        List givenTypeArguments = List.of(givenFirstTypeArgument, mock(TypeMirror.class));
        when(givenType.getTypeArguments()).thenReturn(givenTypeArguments);

        TypeMirror actual = getFirstTypeArgument(givenType);
        assertSame(givenFirstTypeArgument, actual);
    }

    @Test
    public void firstTypeArgumentShouldNotBeGot() {
        DeclaredType givenType = mock(DeclaredType.class);

        when(givenType.getTypeArguments()).thenReturn(emptyList());

        assertThrows(NoSuchElementException.class, () -> getFirstTypeArgument(givenType));
    }
}
