package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

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
}
