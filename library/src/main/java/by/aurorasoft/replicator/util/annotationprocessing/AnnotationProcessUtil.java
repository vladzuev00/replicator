package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.ElementUtils;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.*;
import static java.util.stream.Stream.iterate;

//TODO: remove
@UtilityClass
public final class AnnotationProcessUtil {

    //TODO: ---------------------------------------------------------------------------
    public static boolean isContainRepository(TypeElement element, Elements elementUtil, Types typeUtil) {
        return iterate(element, e -> !ElementUtils.isObject(e), e -> getErasuredTypeElement(e.getSuperclass(), elementUtil, typeUtil))
                .flatMap(e -> e.getEnclosedElements().stream())
                .anyMatch(e -> isJpaRepositoryField(e, elementUtil, typeUtil));
    }

    public static boolean isContainIdGetter(VariableElement element, Elements elementUtil) {
        return ElementUtil.isContainIdGetter(getTypeElement(element, elementUtil));
    }
}
