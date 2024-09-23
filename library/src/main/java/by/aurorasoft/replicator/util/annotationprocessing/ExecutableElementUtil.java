package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.ExecutableElement;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isPublic;
import static javax.lang.model.element.ElementKind.METHOD;
import static org.checkerframework.javacutil.ElementUtils.isStatic;

@UtilityClass
public final class ExecutableElementUtil {
    static final String ID_GETTER_NAME = "getId";

    public static boolean isIdGetter(ExecutableElement element) {
        return element.getKind() == METHOD
                && isPublic(element)
                && !isStatic(element)
                && element.getSimpleName().contentEquals(ID_GETTER_NAME)
                && element.getParameters().isEmpty();
    }
}
