package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.TypesUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.lang.model.type.TypeKind.VOID;
import static org.checkerframework.javacutil.TypesUtils.isPrimitive;

@UtilityClass
public final class TypeMirrorUtil {
    static final String LIST_TYPE_NAME = "java.util.List";
    static final String ITERABLE_TYPE_NAME = "java.lang.Iterable";
    static final String JPA_REPOSITORY_TYPE_NAME = "org.springframework.data.jpa.repository.JpaRepository";

    public static boolean isVoid(TypeMirror mirror) {
        return mirror.getKind() == VOID;
    }

    public static boolean isList(TypeMirror mirror, Elements elementUtil, Types typeUtil) {
        return isErasedSubtype(mirror, LIST_TYPE_NAME, elementUtil, typeUtil);
    }

    public static boolean isIterable(TypeMirror mirror, Elements elementUtil, Types typeUtil) {
        return isErasedSubtype(mirror, ITERABLE_TYPE_NAME, elementUtil, typeUtil);
    }

    public static boolean isJpaRepository(TypeMirror mirror, Elements elementUtil, Types typeUtil) {
        return isErasedSubtype(mirror, JPA_REPOSITORY_TYPE_NAME, elementUtil, typeUtil);
    }

    //TODO: test
    public static boolean isContainIdGetter(TypeMirror mirror, Elements elementUtil, Types typeUtil) {
        return !isVoid(mirror)
                && !isPrimitive(mirror)
                && TypeElementUtil.isContainIdGetter(getTypeElement(mirror, elementUtil), elementUtil, typeUtil);
    }

    public static TypeElement getErasuredTypeElement(TypeMirror mirror, Elements elementUtil, Types typeUtil) {
        return elementUtil.getTypeElement(typeUtil.erasure(mirror).toString());
    }

    //TODO: test
    public static TypeElement getTypeElement(TypeMirror mirror, Elements elementUtil) {
        return elementUtil.getTypeElement(mirror.toString());
    }

    private static boolean isErasedSubtype(TypeMirror mirror,
                                           String superTypeName,
                                           Elements elementUtil,
                                           Types typeUtil) {
        TypeMirror superTypeMirror = elementUtil.getTypeElement(superTypeName).asType();
        return TypesUtils.isErasedSubtype(mirror, superTypeMirror, typeUtil);
    }
}
