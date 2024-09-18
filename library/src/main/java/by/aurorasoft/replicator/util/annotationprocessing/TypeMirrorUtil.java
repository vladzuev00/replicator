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
    private static final String LIST_TYPE_NAME = "java.util.List";
    private static final String ITERABLE_TYPE_NAME = "java.lang.Iterable";
    private static final String JPA_REPOSITORY_TYPE_NAME = "org.springframework.data.jpa.repository.JpaRepository";

    public static boolean isList(TypeMirror mirror, ProcessingEnvironment environment) {
        return isErasedSubtype(mirror, LIST_TYPE_NAME, environment);
    }

    public static boolean isList(TypeElement mirror, ProcessingEnvironment environment) {
        return isErasedSubtype(mirror.asType(), LIST_TYPE_NAME, environment);
    }

    public static boolean isIterable(TypeMirror mirror, ProcessingEnvironment environment) {
        return isErasedSubtype(mirror, ITERABLE_TYPE_NAME, environment);
    }

    public static boolean isJpaRepository(TypeMirror mirror, ProcessingEnvironment environment) {
        return isErasedSubtype(mirror, JPA_REPOSITORY_TYPE_NAME, environment);
    }

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

    public static boolean isContainRepository(TypeMirror mirror, ProcessingEnvironment environment) {
        // TypesUtils.getTypeElement()
        return ElementUtil.getInheritance(environment.getElementUtils().getTypeElement(mirror.toString()), environment)
                .map(m -> environment.getElementUtils().getTypeElement(m.toString()))
                .flatMap(e -> e.getEnclosedElements().stream())
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.FIELD)
                .filter(enclosedElement -> enclosedElement.getSimpleName().contentEquals("repository"))
                .map(Element::asType)
                .filter(type -> isJpaRepository(type, environment))
                .findFirst()
                .isPresent();
    }

    private static boolean isErasedSubtype(TypeMirror subtype, String supertypeName, ProcessingEnvironment environment) {
        TypeMirror supertype = environment.getElementUtils()
                .getTypeElement(supertypeName)
                .asType();
        return TypesUtils.isErasedSubtype(subtype, supertype, environment.getTypeUtils());
    }
}
