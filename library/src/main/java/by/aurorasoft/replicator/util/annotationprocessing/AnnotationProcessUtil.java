package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.TypesUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import java.util.stream.Stream;

import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.checkerframework.javacutil.ElementUtils.getAllSupertypes;

@UtilityClass
public final class AnnotationProcessUtil {
    private static final String JPA_REPOSITORY_FIELD_NAME = "repository";
    private static final String JPA_REPOSITORY_TYPE_NAME = "org.springframework.data.jpa.repository.JpaRepository";
    private static final String LIST_TYPE_NAME = "java.util.List";
    private static final String ITERABLE_TYPE_NAME = "java.lang.Iterable";

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

    public static boolean isContainRepository(TypeElement element, ProcessingEnvironment environment) {
        return getAllSupertypes(element, environment)
                .stream()
                .flatMap(e -> e.getEnclosedElements().stream())
                .anyMatch(e -> isJpaRepositoryField(e, environment));
    }

    public static boolean isList(Element element, ProcessingEnvironment environment) {
        return isErasedSubtype(element, LIST_TYPE_NAME, environment);
    }

    public static boolean isIterable(Element element, ProcessingEnvironment environment) {
        return isErasedSubtype(element, ITERABLE_TYPE_NAME, environment);
    }

    public static TypeMirror getFirstTypeArgument(Element element) {
        if (element.asType() instanceof DeclaredType declaredType) {
            return declaredType.getTypeArguments().get(0);
        }
        throw new IllegalArgumentException("Impossible to extract first type argument of '%s'".formatted(element));
    }

    static final String GETTER_NAME_ID = "getId";

    //TODO: check static, check parameters
    public static boolean isIdGetter(Element element) {
        return element.getKind() == METHOD
                && isPublic(element)
                && element.getSimpleName().contentEquals(GETTER_NAME_ID);
    }

    //TODO: ElementUtils.matchesElement
    public static boolean isContainIdGetter(Element element) {
        return element.getEnclosedElements()
                .stream()
                //TODO same as ElementUtils.isGetter
                .anyMatch(ElementUtil::isIdGetter);
    }

    //TODO: temp
    public static boolean isContainIdGetter(VariableElement element, ProcessingEnvironment environment) {
        return isContainIdGetter(environment.getElementUtils().getTypeElement(element.asType().toString()));
    }

    public static boolean isContainIdGetter(TypeMirror element, ProcessingEnvironment environment) {
        return isContainIdGetter(environment.getElementUtils().getTypeElement(element.toString()));
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
