package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.Modifier.PUBLIC;

@UtilityClass
public final class ElementUtil {
    static final String ITERABLE_TYPE_NAME = "java.lang.Iterable";
    static final String JPA_REPOSITORY_TYPE_NAME = "org.springframework.data.jpa.repository.JpaRepository";
    static final String JPA_REPOSITORY_FIELD_NAME = "repository";

    public static boolean isPublic(Element element) {
        return element.getModifiers().contains(PUBLIC);
    }

    public static boolean isClass(Element element) {
        return element.getKind() == CLASS;
    }

//    public static boolean isPackage(Element element) {
//        return element.getKind() == PACKAGE;
//    }
//
//    public static boolean isContainIdGetter(Element element) {
//        return element.getEnclosedElements()
//                .stream()
//                .filter(e -> e instanceof ExecutableElement)
//                .map(e -> (ExecutableElement) e)
//                .anyMatch(AnnotationProcessUtil::isIdGetter);
//    }
//
//    public static boolean isIterable(Element element, Elements elementUtil, Types typeUtil) {
//        return isErasedSubtype(element, ITERABLE_TYPE_NAME, elementUtil, typeUtil);
//    }
//
//    public static boolean isJpaRepository(Element element, Elements elementUtil, Types typeUtil) {
//        return isErasedSubtype(element, JPA_REPOSITORY_TYPE_NAME, elementUtil, typeUtil);
//    }
//
//    public static boolean isErasedSubtype(Element element,
//                                          String supertypeName,
//                                          Elements elementUtil,
//                                          Types typeUtil) {
//        return AnnotationProcessUtil.isErasedSubtype(element.asType(), supertypeName, elementUtil, typeUtil);
//    }
//
//    public static boolean isJpaRepositoryField(Element element, Elements elementUtil, Types typeUtil) {
//        return element.getKind() == FIELD
//                && element.getSimpleName().contentEquals(JPA_REPOSITORY_FIELD_NAME)
//                && isJpaRepository(element, elementUtil, typeUtil);
//    }
}
