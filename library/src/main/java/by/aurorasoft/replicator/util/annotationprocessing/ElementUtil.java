package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.lang.model.element.ElementKind.FIELD;

@UtilityClass
public final class ElementUtil {
    static final String JPA_REPOSITORY_FIELD_NAME = "repository";

    public static boolean isIterable(Element element, Elements elementUtil, Types typeUtil) {
        return TypeMirrorUtil.isIterable(element.asType(), elementUtil, typeUtil);
    }

    public static boolean isJpaRepository(Element element, Elements elementUtil, Types typeUtil) {
        return TypeMirrorUtil.isJpaRepository(element.asType(), elementUtil, typeUtil);
    }

    public static boolean isContainIdGetter(Element element) {
        return element.getEnclosedElements()
                .stream()
                .filter(e -> e instanceof ExecutableElement)
                .map(e -> (ExecutableElement) e)
                .anyMatch(ExecutableElementUtil::isIdGetter);
    }

    public static boolean isJpaRepositoryField(Element element, Elements elementUtil, Types typeUtil) {
        return element.getKind() == FIELD
                && element.getSimpleName().contentEquals(JPA_REPOSITORY_FIELD_NAME)
                && isJpaRepository(element, elementUtil, typeUtil);
    }
}
