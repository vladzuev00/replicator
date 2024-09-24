package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.TypeParameterElement;

@UtilityClass
public final class TypeParameterElementUtil {

    public static boolean isContainIdGetter(TypeParameterElement element) {
        return ElementUtil.isContainIdGetter(element.getGenericElement());
    }
}
