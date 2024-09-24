package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

@UtilityClass
public final class DeclaredTypeUtil {

    //TODO: test
    public static TypeMirror getFirstTypeArgument(DeclaredType type) {
        List<? extends TypeMirror> arguments = type.getTypeArguments();
        if (arguments.isEmpty()) {
            throw new IllegalArgumentException("There are no type arguments in '%s'".formatted(type));
        }
        return arguments.get(0);
    }
}
