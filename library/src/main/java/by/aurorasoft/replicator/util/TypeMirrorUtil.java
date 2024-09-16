package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

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
            throw new IllegalArgumentException("Primitive types or void don't have type arguments");
        }
        return DeclaredTypeUtil.getFirstTypeArgument((DeclaredType) mirror);
    }

    public static boolean isContainIdGetter(TypeMirror mirror, ProcessingEnvironment environment) {
        if (isPrimitiveOrVoid(mirror)) {
            return false;
        }
        Element element = environment.getTypeUtils().asElement(mirror);
        return ElementUtil.isContainIdGetter(element);
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
}
