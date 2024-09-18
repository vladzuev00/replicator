package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.TypesUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static javax.lang.model.type.TypeKind.VOID;

@UtilityClass
public final class TypeMirrorUtil {

    public static boolean isPrimitiveOrVoid(TypeMirror mirror) {
        TypeKind kind = mirror.getKind();
        return kind.isPrimitive() || kind == VOID;
    }

    public static TypeMirror getFirstTypeArgument(TypeElement mirror) {
        if (mirror.asType() instanceof DeclaredType declaredType) {
            return declaredType.getTypeArguments().get(0);
        }
        throw new IllegalArgumentException("Impossible to extract first type argument of '%s'".formatted(mirror));
    }

    public static boolean isContainIdGetter(TypeMirror mirror, ProcessingEnvironment environment) {
        if (isPrimitiveOrVoid(mirror)) {
            return false;
        }
        Element element = environment.getTypeUtils().asElement(mirror);
        return ElementUtil.isContainIdGetter(element);
    }
}
