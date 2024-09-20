package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.TypesUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.checkerframework.javacutil.ElementUtils.isStatic;

@UtilityClass
public final class AnnotationProcessUtil {
    private static final String LIST_TYPE_NAME = "java.util.List";
    private static final String ITERABLE_TYPE_NAME = "java.lang.Iterable";
    private static final String ID_GETTER_NAME = "getId";
    private static final String JPA_REPOSITORY_FIELD_NAME = "repository";
    private static final String JPA_REPOSITORY_TYPE_NAME = "org.springframework.data.jpa.repository.JpaRepository";

    public static <E extends Element> Stream<E> getAnnotatedElements(TypeElement annotation,
                                                                     RoundEnvironment environment,
                                                                     Class<E> elementType) {
        return environment.getElementsAnnotatedWith(annotation)
                .stream()
                .map(elementType::cast);
    }

    public static boolean isPublic(Element element) {
        return element.getModifiers().contains(PUBLIC);
    }

    public static boolean isReplicatedService(Element element) {
        return element.getAnnotation(ReplicatedService.class) != null;
    }

    public static TypeElement getTypeElement(TypeMirror mirror, ProcessingEnvironment environment) {
        return environment.getElementUtils().getTypeElement(mirror.toString());
    }

    public static TypeElement getEnclosingClass(ExecutableElement element) {
        return (TypeElement) element.getEnclosingElement();
    }

    public static DeclaredType getReturnType(ExecutableElement element) {
        return (DeclaredType) element.getReturnType();
    }

    public static boolean isContainRepository(TypeElement element, ProcessingEnvironment environment) {
        return Stream.iterate(element, e -> !ElementUtils.isObject(e), e -> environment.getElementUtils().getTypeElement(environment.getTypeUtils().erasure(e.getSuperclass()).toString()))
                .flatMap(e -> e.getEnclosedElements().stream())
                .anyMatch(e -> isJpaRepositoryField(e, environment));
    }

    public static boolean isList(TypeMirror type, ProcessingEnvironment environment) {
        TypeMirror supertype = environment.getElementUtils()
                .getTypeElement(LIST_TYPE_NAME)
                .asType();
        return TypesUtils.isErasedSubtype(type, supertype, environment.getTypeUtils());
    }

    public static boolean isList(Element element, ProcessingEnvironment environment) {
        return isErasedSubtype(element, LIST_TYPE_NAME, environment);
    }

    public static boolean isIterable(Element element, ProcessingEnvironment environment) {
        return isErasedSubtype(element, ITERABLE_TYPE_NAME, environment);
    }

    //TODO: refactor
    public static TypeMirror getFirstTypeArgument(Element element) {
        if (element.asType() instanceof DeclaredType declaredType) {
            List<? extends TypeMirror> arguments = declaredType.getTypeArguments();
            if (!arguments.isEmpty()) {
                return arguments.get(0);
            }
        }
        throw new IllegalArgumentException("Impossible to extract first type argument of '%s'".formatted(element));
    }

    public static TypeMirror getFirstTypeArgument(TypeMirror mirror) {
        if (mirror instanceof DeclaredType declaredType) {
            List<? extends TypeMirror> arguments = declaredType.getTypeArguments();
            if (!arguments.isEmpty()) {
                return arguments.get(0);
            }
        }
        throw new IllegalArgumentException("Impossible to extract first type argument of '%s'".formatted(mirror));
    }

    public static TypeMirror getFirstTypeArgument(DeclaredType type) {
        List<? extends TypeMirror> arguments = type.getTypeArguments();
        if (!arguments.isEmpty()) {
            return arguments.get(0);
        }
        throw new IllegalArgumentException("Impossible to extract first type argument of '%s'".formatted(type));
    }

    public static boolean isIdGetter(ExecutableElement element) {
        return element.getKind() == METHOD
                && isPublic(element)
                && !isStatic(element)
                && element.getSimpleName().contentEquals(ID_GETTER_NAME)
                && element.getParameters().isEmpty();
    }

    public static boolean isContainIdGetter(Element element) {
        return element.getEnclosedElements()
                .stream()
                .filter(e -> e instanceof ExecutableElement)
                .map(e -> (ExecutableElement) e)
                .anyMatch(AnnotationProcessUtil::isIdGetter);
    }

    public static boolean isContainIdGetter(VariableElement element, ProcessingEnvironment environment) {
        return isContainIdGetter(requireNonNull(getTypeElement(element.asType(), environment)));
    }

    public static boolean isContainIdGetter(TypeMirror mirror, ProcessingEnvironment environment) {
        if (mirror.getKind() == TypeKind.VOID || mirror.getKind().isPrimitive()) {
            return false;
        }
        return isContainIdGetter(environment.getTypeUtils().asElement(mirror));
    }

    public static boolean isContainIdGetter(TypeParameterElement element) {
        return isContainIdGetter(element.getGenericElement());
    }

    private static boolean isJpaRepositoryField(Element element, ProcessingEnvironment environment) {
        return element.getKind() == FIELD
                && element.getSimpleName().contentEquals(JPA_REPOSITORY_FIELD_NAME)
                && isJpaRepository(element, environment);
    }

    private static boolean isJpaRepository(Element element, ProcessingEnvironment environment) {
        return isErasedSubtype(element, JPA_REPOSITORY_TYPE_NAME, environment);
    }

    private static boolean isErasedSubtype(Element element, String supertypeName, ProcessingEnvironment environment) {
        TypeMirror supertype = environment.getElementUtils()
                .getTypeElement(supertypeName)
                .asType();
        return TypesUtils.isErasedSubtype(element.asType(), supertype, environment.getTypeUtils());
    }
}
