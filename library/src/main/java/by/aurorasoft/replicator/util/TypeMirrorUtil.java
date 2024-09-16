package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.NoSuchElementException;

import static javax.lang.model.type.TypeKind.VOID;

@UtilityClass
public final class TypeMirrorUtil {
    private static final String LIST_CANONICAL_NAME = "java.util.List";
    private static final String ITERABLE_CANONICAL_NAME = "java.lang.Iterable";

    public static boolean isList(TypeMirror mirror, ProcessingEnvironment environment) {
        return isErasedMirrorAssignable(mirror, LIST_CANONICAL_NAME, environment);
    }

    public static boolean isIterable(TypeMirror mirror, ProcessingEnvironment environment) {
        return isErasedMirrorAssignable(mirror, ITERABLE_CANONICAL_NAME, environment);
    }

    public static TypeMirror getFirstTypeArgument(TypeMirror mirror) {
        if (isPrimitiveOrVoid(mirror)) {
            throwNoTypeArgumentsException(mirror);
        }
        return getFirstTypeArgument((DeclaredType) mirror);
    }

    private static boolean isErasedMirrorAssignable(TypeMirror mirror,
                                                    String superTypeName,
                                                    ProcessingEnvironment environment) {
        Types typeUtil = environment.getTypeUtils();
        TypeMirror erasedMirror = typeUtil.erasure(mirror);
        TypeMirror superTypeMirror = environment.getElementUtils()
                .getTypeElement(superTypeName)
                .asType();
        return typeUtil.isAssignable(erasedMirror, superTypeMirror);
    }

    private static boolean isPrimitiveOrVoid(TypeMirror mirror) {
        TypeKind kind = mirror.getKind();
        return kind.isPrimitive() || kind == VOID;
    }

    private static TypeMirror getFirstTypeArgument(DeclaredType type) {
        List<? extends TypeMirror> arguments = type.getTypeArguments();
        if (arguments.isEmpty()) {
            throwNoTypeArgumentsException(type);
        }
        return arguments.get(0);
    }

    private static void throwNoTypeArgumentsException(TypeMirror mirror) {
        throw new NoSuchElementException("%s doesn't have type arguments".formatted(mirror));
    }
}
