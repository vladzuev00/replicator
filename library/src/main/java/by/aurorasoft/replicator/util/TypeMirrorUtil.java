package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

@UtilityClass
public final class TypeMirrorUtil {
    private static final String LIST_CANONICAL_NAME = "java.util.List";

    public static boolean isList(TypeMirror mirror, ProcessingEnvironment environment) {
        Types typeUtil = environment.getTypeUtils();
        TypeMirror erasedMirror = typeUtil.erasure(mirror);
        TypeMirror listMirror = createListMirror(environment);
        return typeUtil.isAssignable(erasedMirror, listMirror);
    }

    private static TypeMirror createListMirror(ProcessingEnvironment environment) {
        return environment.getElementUtils().getTypeElement(LIST_CANONICAL_NAME).asType();
    }
}
