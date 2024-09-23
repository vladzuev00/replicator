package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.TypesUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isPackage;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isVoid;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.iterate;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.type.TypeKind.VOID;
import static org.checkerframework.javacutil.TypesUtils.isPrimitive;

@UtilityClass
public final class AnnotationProcessUtil {
    static final String ITERABLE_TYPE_NAME = "java.lang.Iterable";
    static final String JPA_REPOSITORY_FIELD_NAME = "repository";
    static final String JPA_REPOSITORY_TYPE_NAME = "org.springframework.data.jpa.repository.JpaRepository";

    public static <E extends Element> Stream<E> getAnnotatedElements(TypeElement annotation,
                                                                     RoundEnvironment environment,
                                                                     Class<E> elementType) {
        return environment.getElementsAnnotatedWith(annotation)
                .stream()
                .map(elementType::cast);
    }

    //TODO: remove and put into TypeElementUtil and Element replace by TypeElement
    public static boolean isReplicatedService(Element element) {
        return element.getAnnotation(ReplicatedService.class) != null;
    }

    //TODO: remove
    public static boolean isContainIdGetter(Element element) {
        return element.getEnclosedElements()
                .stream()
                .filter(e -> e instanceof ExecutableElement)
                .map(e -> (ExecutableElement) e)
                .anyMatch(ExecutableElementUtil::isIdGetter);
    }

    public static boolean isContainIdGetter(TypeMirror mirror, Types typeUtil) {
        return !isVoid(mirror)
                && !isPrimitive(mirror)
                && isContainIdGetter(typeUtil.asElement(mirror));
    }

    public static boolean isContainIdGetter(TypeParameterElement element) {
        return isContainIdGetter(element.getGenericElement());
    }

    //TODO: remove
    public static boolean isIterable(Element element, Elements elementUtil, Types typeUtil) {
        return isErasedSubtype(element, ITERABLE_TYPE_NAME, elementUtil, typeUtil);
    }

    //TODO: remove
    public static boolean isJpaRepository(Element element, Elements elementUtil, Types typeUtil) {
        return isErasedSubtype(element, JPA_REPOSITORY_TYPE_NAME, elementUtil, typeUtil);
    }

    //TODO: ---------------------------------------------------------------------------
    public static boolean isContainRepository(TypeElement element, Elements elementUtil, Types typeUtil) {
        return iterate(element, e -> !ElementUtils.isObject(e), e -> elementUtil.getTypeElement(typeUtil.erasure(e.getSuperclass()).toString()))
                .flatMap(e -> e.getEnclosedElements().stream())
                .anyMatch(e -> isJpaRepositoryField(e, elementUtil, typeUtil));
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

    public static boolean isContainIdGetter(VariableElement element, Elements elementUtil) {
        return isContainIdGetter(requireNonNull(getTypeElement(element.asType(), elementUtil)));
    }

    public static TypeElement getTypeElement(TypeMirror mirror, Elements elementUtil) {
        return elementUtil.getTypeElement(mirror.toString());
    }

    //TODO: remove
    public static boolean isErasedSubtype(Element element,
                                          String supertypeName,
                                          Elements elementUtil,
                                          Types typeUtil) {
        return true;
//        return TypeMirrorUtil.isErasedSubtype(element.asType(), supertypeName, elementUtil, typeUtil);
    }

    //TODO: remove
    private static boolean isJpaRepositoryField(Element element, Elements elementUtil, Types typeUtil) {
        return element.getKind() == FIELD
                && element.getSimpleName().contentEquals(JPA_REPOSITORY_FIELD_NAME)
                && isJpaRepository(element, elementUtil, typeUtil);
    }
}
