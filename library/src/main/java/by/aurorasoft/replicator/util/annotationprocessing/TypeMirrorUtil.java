package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.TypesUtils;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.lang.model.type.TypeKind.VOID;

@UtilityClass
public final class TypeMirrorUtil {

    public static boolean isVoid(TypeMirror mirror) {
        return mirror.getKind() == VOID;
    }

    public static boolean isErasedSubtype(TypeMirror mirror,
                                          String superTypeName,
                                          Elements elementUtil,
                                          Types typeUtil) {
        TypeMirror superTypeMirror = elementUtil.getTypeElement(superTypeName).asType();
        return TypesUtils.isErasedSubtype(mirror, superTypeMirror, typeUtil);
    }
}
