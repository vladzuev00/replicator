package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.lang.model.type.TypeMirror;

import static javax.lang.model.type.TypeKind.VOID;

@UtilityClass
public final class TypeMirrorUtil {

    public static boolean isVoid(TypeMirror mirror) {
        return mirror.getKind() == VOID;
    }
}
