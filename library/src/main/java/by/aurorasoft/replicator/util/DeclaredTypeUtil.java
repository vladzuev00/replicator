package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.NoSuchElementException;

@UtilityClass
public final class DeclaredTypeUtil {

    public static TypeMirror getFirstTypeArgument(DeclaredType type) {
        List<? extends TypeMirror> arguments = type.getTypeArguments();
        if (arguments.isEmpty()) {
            throw new NoSuchElementException("%s doesn't have type arguments".formatted(type));
        }
        return arguments.get(0);
    }
}
