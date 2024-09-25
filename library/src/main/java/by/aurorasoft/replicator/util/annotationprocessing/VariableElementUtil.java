package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;

@UtilityClass
public final class VariableElementUtil {

    //TODO: test
    public static boolean isContainIdGetter(VariableElement element, Types typeUtil) {
        return TypeMirrorUtil.isContainIdGetter(element.asType(), typeUtil);
    }
}
