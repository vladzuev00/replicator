package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@UtilityClass
public final class VariableElementUtil {

    public static boolean isContainIdGetter(VariableElement element, Elements elementUtil, Types typeUtil) {
        return TypeMirrorUtil.isContainIdGetter(element.asType(), elementUtil, typeUtil);
    }
}
