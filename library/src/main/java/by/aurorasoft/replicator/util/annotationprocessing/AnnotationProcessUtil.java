package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.TypesUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static javax.lang.model.element.ElementKind.FIELD;
import static org.checkerframework.javacutil.ElementUtils.getAllSupertypes;

@UtilityClass
public final class AnnotationProcessUtil {
    private static final String JPA_REPOSITORY_FIELD_NAME = "repository";
    private static final String JPA_REPOSITORY_TYPE_NAME = "org.springframework.data.jpa.repository.JpaRepository";
    private static final String LIST_TYPE_NAME = "java.util.List";
    private static final String ITERABLE_TYPE_NAME = "java.lang.Iterable";

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
