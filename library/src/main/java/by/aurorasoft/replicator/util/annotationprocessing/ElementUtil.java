package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@UtilityClass
public final class ElementUtil {

    public static boolean isIterable(Element element, Elements elementUtil, Types typeUtil) {
        return TypeMirrorUtil.isIterable(element.asType(), elementUtil, typeUtil);
    }

    public static boolean isJpaRepository(Element element, Elements elementUtil, Types typeUtil) {
        return TypeMirrorUtil.isJpaRepository(element.asType(), elementUtil, typeUtil);
    }
}
