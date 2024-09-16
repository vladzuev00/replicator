package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import static javax.lang.model.element.Modifier.PUBLIC;
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
            throw new IllegalArgumentException("%s doesn't have type arguments".formatted(mirror));
        }
        return DeclaredTypeUtil.getFirstTypeArgument((DeclaredType) mirror);
    }

    public static boolean isContainIdGetter(TypeMirror typeMirror, ProcessingEnvironment environment) {
        if (typeMirror.getKind().isPrimitive() || typeMirror.getKind() == TypeKind.VOID) {
            return false;
        }
        return environment.getTypeUtils().asElement(typeMirror)
                .getEnclosedElements()
                .stream()
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.METHOD)
                .filter(enclosedElement -> enclosedElement.getSimpleName().contentEquals("getId"))
                .filter(enclosedElement -> enclosedElement.getModifiers().contains(PUBLIC))
                .findFirst()
                .isPresent();
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
