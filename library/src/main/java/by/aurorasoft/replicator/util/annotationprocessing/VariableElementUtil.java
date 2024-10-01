package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isJpaRepository;
import static javax.lang.model.element.ElementKind.FIELD;

@UtilityClass
public final class VariableElementUtil {
    static final String JPA_REPOSITORY_FIELD_NAME = "repository";

    public static boolean isContainIdGetter(VariableElement element, Elements elementUtil, Types typeUtil) {
        return TypeMirrorUtil.isContainIdGetter(element.asType(), elementUtil, typeUtil);
    }

    public static boolean isJpaRepositoryField(VariableElement element, Elements elementUtil, Types typeUtil) {
        return element.getKind() == FIELD
                && element.getSimpleName().contentEquals(JPA_REPOSITORY_FIELD_NAME)
                && isJpaRepository(element, elementUtil, typeUtil);
    }
}
