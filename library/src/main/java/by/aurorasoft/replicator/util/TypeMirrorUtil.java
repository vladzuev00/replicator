package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import java.util.stream.Stream;

import static java.util.stream.Stream.concat;
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

    public static boolean isContainRepository(TypeMirror mirror, ProcessingEnvironment environment) {
        return concat(environment.getTypeUtils().directSupertypes(mirror).stream(), Stream.of(mirror)).flatMap(superType -> environment.getTypeUtils().asElement(superType).getEnclosedElements().stream())
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.FIELD)
                .filter(enclosedElement -> enclosedElement.getSimpleName().contentEquals("repository"))
                .filter(enclosedElement -> isRepository(enclosedElement, environment))
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

    private static boolean isRepository(Element element, ProcessingEnvironment environment) {
        return environment.getTypeUtils().isAssignable(environment.getTypeUtils().erasure(element.asType()), environment.getElementUtils().getTypeElement("org.springframework.data.jpa.repository.JpaRepository").asType());
    }
}
